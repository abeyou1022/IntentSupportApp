package com.example.intentsupportapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intentsupportapp.R;
import com.example.intentsupportapp.dto.IntentExtraDto;
import com.example.intentsupportapp.enums.IntentExtraKind;

import java.util.List;

/*
 * インテント送信画面
 */
public class SendIntentActivity extends AppCompatActivity {

    //エクストラ一覧(ListView)のアダプター
    private ExtraListAdapter mExtraListAdapter;

    //遷移先アプリのパッケージ名
    private String mPackageName;

    //メインレイアウト
    private ConstraintLayout mMainLayout;
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_intent_activity);

        // インテントから遷移先アプリのパッケージ名を取得
        Intent intent = getIntent();
        mPackageName = intent.getStringExtra("EXTRA_PACKAGE_NAME");
        Log.d(getPackageName(), "packageName : " + mPackageName);

        // 遷移先アプリが持つアクティビティを取得
        List<ResolveInfo> activityList = getActivities();
        // アクティビティがない場合、トーストを表示して元の前の画面に戻る
        if (null == activityList || activityList.size() < 1) {
            Log.d(getPackageName(), "No Activity");
            Toast.makeText(this, "起動できるアクティビティがありません", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 各Viewの登録
        //メインレイアウト
        mMainLayout = (ConstraintLayout) findViewById(R.id.mainLayout);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //エクストラ一覧
        ListView listView = findViewById(R.id.listExtra);
        mExtraListAdapter = new ExtraListAdapter(this, 0);
        listView.setAdapter(mExtraListAdapter); //ListViewにアダプターを設定
        mExtraListAdapter.notifyDataSetChanged();

        //エクストラ追加ボタン
        Button paramAddBtn = findViewById(R.id.buttonAddExtra);
        paramAddBtn.setOnClickListener(view -> {
            addExtraAdapter();
        });

        //Intent起動ボタン
        Button startIntentBtn = findViewById(R.id.buttonStartIntent);
        startIntentBtn.setOnClickListener(view -> {
            try {
                startIntent();
            } catch (Exception e) {
                Log.e(getPackageName(), "Intent送信失敗", e);
                Toast.makeText(this, "Intent送信失敗", Toast.LENGTH_SHORT).show();
            }
        });

        //タイトル
        TextView title = findViewById(R.id.sendIntentTitle);
        title.setText(getAppName()); //表示文字列を遷移先アプリ名に設定

        //起動アクティビティ選択プルダウン
        setActivitySpinner(activityList);

        //エクストラ型選択プルダウン
        setExtraTypeSpinner();
    }

    /*
     * アプリ名取得
     */
    private String getAppName() {
        PackageManager packageManager = getPackageManager();
        ApplicationInfo packageInfo;
        try {
            packageInfo = packageManager.getApplicationInfo(mPackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        //packageManagerからアプリ名を取得
        String appName = (String) packageManager.getApplicationLabel(packageInfo);
        Log.d(getPackageName(), appName);

        return appName;
    }

    /*
     * アプリが持つActivityの情報取得
     */
    private List getActivities() {
        Intent intent = new Intent();
        intent.setPackage(mPackageName);

        //アプリが持つアクティビティを取得
        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, PackageManager.GET_META_DATA);

        return activities;

    }

    /*
     * 起動アクティビティ選択プルダウンに要素を設定
     */
    private void setActivitySpinner(List<ResolveInfo> activityList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerActivity);

        for (ResolveInfo activity : activityList) {
            adapter.add(activity.activityInfo.name);
        }

        spinner.setAdapter(adapter);
    }

    /*
     * エクストラ型選択プルダウンに要素を設定
     */
    private void setExtraTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerExtraType);

        for (IntentExtraKind type : IntentExtraKind.values()) {
            adapter.add(type.toString());
        }

        spinner.setAdapter(adapter);
    }

    /*
     * EditText編集時に背景をタップしたらキーボードを閉じるようにするタッチイベントの処理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //キーボードを隠す
        mInputMethodManager.hideSoftInputFromWindow(mMainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        mMainLayout.requestFocus();
        return false;
    }

    /*
     * エクストラ一覧に要素を追加
     */
    private void addExtraAdapter() {
        //入力内容からIntentExtraDtoオブジェクトを生成する
        IntentExtraDto dto = createIntentExtraDto();
        if (null == dto) {
            Toast.makeText(this, "Extra情報を入力してください", Toast.LENGTH_SHORT).show();
            return;
        }

        //ExtraListAdapterに追加済みのパラメータの場合処理しない
        for (int i = 0; i < mExtraListAdapter.getCount(); i++) {
            IntentExtraDto extra = (IntentExtraDto) mExtraListAdapter.getItem(i);
            if (extra.equals(dto)) {
                return;
            }
        }

        //ListViewにIntentExtraDtoオブジェクトを追加
        mExtraListAdapter.add(dto);
        //ListView再描画
        mExtraListAdapter.notifyDataSetChanged();
    }

    /*
     * インテント送信処理
     */
    private void startIntent() throws NumberFormatException{
        //Intentを生成
        Intent intent = new Intent();
        Spinner extraType = (Spinner) findViewById(R.id.spinnerActivity);
        String className = extraType.getSelectedItem().toString();
        intent.setClassName(mPackageName, className);

        //Intentにパラメータを付与する
        for (int i = 0; i < mExtraListAdapter.getCount(); i++) {
            IntentExtraDto extra = (IntentExtraDto) mExtraListAdapter.getItem(i);
            switch(extra.extraKind) {
                case INT:
                    int param = Integer.parseInt(extra.value);
                    intent.putExtra(extra.key, param);
                    break;
                case STRING:
                    intent.putExtra(extra.key, extra.value);
                    break;
                default:
                    //no operation
                    break;
            }
        }

        //Intent起動
        startActivity(intent);
    }

    private IntentExtraDto createIntentExtraDto() {

        //Extraの型を取得する
        Spinner extraType = (Spinner) findViewById(R.id.spinnerExtraType);
        String type = extraType.getSelectedItem().toString();
        IntentExtraKind extraKind = IntentExtraKind.valueOf(type);

        //Extraのキーを取得する
        EditText extraKey = (EditText) findViewById(R.id.editorExtraKey);
        String key = extraKey.getText().toString();

        //Extraの値を取得する
        EditText extraValue = (EditText) findViewById(R.id.editorExtraValue);
        String value = extraValue.getText().toString();

        /*
         * 取得したExtraの情報のうち、空が含まれる場合nullを返す
         * ※型情報が空になることはないため、キーと値のみチェックする
         */
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return null;
        }

        //IntentExtraDtoオブジェクトを生成
        IntentExtraDto dto = new IntentExtraDto(extraKind, key, value);
        return dto;
    }

    /*
     * エクストラリスト用アダプター
     */
    class ExtraListAdapter extends ArrayAdapter {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        public ExtraListAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ViewHolder viewHolder;
            View view = convertView;

            if (null == view) {
                view = inflater.inflate(R.layout.row_intent_extra, parent, false);
                viewHolder = new ViewHolder(view.findViewById(R.id.item_param_value), view.findViewById(R.id.delete_button));
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            IntentExtraDto listItem = (IntentExtraDto) getItem(position);
            viewHolder.itemParamValueView.setText(listItem.toString());
            viewHolder.deleteButton.setOnClickListener(view1 -> {
                remove(listItem);
                notifyDataSetChanged();
            });

            return view;
        }
    }

    /*
     * インテント一覧用アダプターのViewHolder
     */
    class ViewHolder {
        TextView itemParamValueView;
        ImageButton deleteButton;

        public ViewHolder(TextView itemParamValueView, ImageButton deleteButton) {
            this.itemParamValueView = itemParamValueView;
            this.deleteButton = deleteButton;
        }
    }
}