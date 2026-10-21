package com.noyn.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private GridView appGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appGrid = findViewById(R.id.app_grid);
        loadApps();
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
        final List<ResolveInfo> apps = new ArrayList<>();
        for (ResolveInfo ri : list) {
            if (!ri.activityInfo.packageName.equals(getPackageName())) {
                apps.add(ri);
            }
        }

        appGrid.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() { return apps.size(); }
            @Override
            public Object getItem(int position) { return apps.get(position); }
            @Override
            public long getItemId(int position) { return position; }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv;
                if (convertView == null) {
                    tv = new TextView(MainActivity.this);
                    tv.setPadding(16, 16, 16, 16);
                    tv.setTextSize(16);
                    tv.setTextColor(0xFFFFFFFF);
                    tv.setFocusable(true);
                } else {
                    tv = (TextView) convertView;
                }

                ResolveInfo info = apps.get(position);
                tv.setText(info.loadLabel(pm));

                tv.setOnClickListener(v -> {
                    Intent launchIntent = pm.getLaunchIntentForPackage(info.activityInfo.packageName);
                    if (launchIntent != null) startActivity(launchIntent);
                });

                return tv;
            }
        });
    }
}
