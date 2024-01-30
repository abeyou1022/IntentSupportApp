# IntentSupportApp

端末にインストールされているアプリの任意のアクティビティを起動するアプリです。  
インテントを送信する際に任意のExtra情報を付与することができ、主に端末のUSBデバッグがオンにできない（PCから操作できない）状況でのテスト時に役立ちます。  

■ユースケース
1. アプリは起動時に端末内にインストールされているアプリを取得し、一覧表示する。（画像1枚目）
2. ユーザーは起動したいアプリを選択する。
3. アプリはアクティビティ起動画面（画像2枚目）を表示する。
4. ユーザーは起動するアクティビティ、インテントに付与するExtra情報を入力し、「ACTIVITY起動」ボタンをタップする。
5. アプリは起動するアクティビティに対してインテントを送信する。

<br>

<p align="center">
  <img src="https://raw.githubusercontent.com/wiki/abeyou1022/IntentSupportApp/images/%E3%82%A2%E3%83%97%E3%83%AA%E4%B8%80%E8%A6%A7%E7%94%BB%E9%9D%A2.jpg" alt="アプリ一覧画面" width="300px">
  <img src="https://raw.githubusercontent.com/wiki/abeyou1022/IntentSupportApp/images/%E3%82%A2%E3%82%AF%E3%83%86%E3%82%A3%E3%83%93%E3%83%86%E3%82%A3%E8%B5%B7%E5%8B%95%E7%94%BB%E9%9D%A2.jpg" alt="アクティビティ起動画面" width="300px">
</p>
