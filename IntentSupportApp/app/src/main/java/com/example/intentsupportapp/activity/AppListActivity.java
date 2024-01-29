package com.example.intentsupportapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.intentsupportapp.dto.AppInfoDto;
import com.example.intentsupportapp.R;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    PackageManager packageManager = null;

    //アプリ一覧用ListView
    ListView listView1;

    //アプリ一覧リスト
    static List<AppInfoDto> dataList = new ArrayList<>();

    //アプリ一覧用アダプター
    static PackageListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        packageManager = getApplicationContext().getPackageManager();

        listView1 = (ListView) findViewById(R.id.listApp);

        adapter = new PackageListAdapter();
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(this);

        updatePackageList();
    }

    /*
     * アプリ一覧表示
     */
    protected void updatePackageList() {
        //リスト初期化
        dataList.clear();

        //インストール済みアプリのパッケージ情報を取得
        List<PackageInfo> pkgInfoList = packageManager.getInstalledPackages(0);

        //インストール済みアプリの数だけループ
        for (PackageInfo pkgInfo : pkgInfoList) {
            //アプリ情報を取得
            ApplicationInfo appInfo = pkgInfo.applicationInfo;

            //ラベルを取得
            String label = appInfo.loadLabel(packageManager).toString();

            //アイコンを取得
            Drawable drawable = appInfo.loadIcon(packageManager);

            //パッケージ情報からパッケージ名を取得
            String packageName = pkgInfo.packageName;

            // リストに要素を追加
            dataList.add(new AppInfoDto(label, drawable, packageName));
        }
        //リスト再描画
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //選択中のアプリを取得
        AppInfoDto appInfo = (AppInfoDto) parent.getItemAtPosition(position);
        Log.d(getPackageName(), "touched : " + appInfo.getLabel());

        //インテント送信画面に遷移
        Intent intent = new Intent(this, SendIntentActivity.class);
        intent.putExtra("EXTRA_PACKAGE_NAME", appInfo.getPackageName());
        startActivity(intent);
    }


    /*
     * アプリ一覧用アダプター
     */
    private class PackageListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(
                int position,
                View convertView,
                ViewGroup parent) {

            View v = convertView;
            TextView textView1;
            ImageView imageView1;

            if (v == null) {
                LayoutInflater inflater =
                        (LayoutInflater) getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.row_app_list, null);
            }

            AppInfoDto aInfo = (AppInfoDto) getItem(position);
            if (aInfo != null) {
                textView1 = (TextView) v.findViewById(R.id.itemAppName);
                imageView1 = (ImageView) v.findViewById(R.id.itemAppIcon);

                textView1.setText(aInfo.getLabel());
                imageView1.setImageDrawable(aInfo.getDrawable());
            }
            return v;
        }
    }
}