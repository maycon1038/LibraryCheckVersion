package br.com.msm.librarycheckversion;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.msm.checkversion.CheckVersion;
import br.com.msm.checkversion.CheckVersionApp;

public class MainActivity extends AppCompatActivity implements CheckVersionApp {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				PackageInfo pInfo = null;
				try {
					pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

					CheckVersion cv = new CheckVersion(MainActivity.this, pInfo);
					cv.getVersionPlayStore(MainActivity.this);
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void newVersionApp(String versionPlayStory, String dateUpdate, String news, String erro) {


		Toast.makeText(this,
			"\n" + dateUpdate +
			"\n" + news +
			"\n" + erro, Toast.LENGTH_SHORT).show();
		Log.d("newVersionApp",versionPlayStory +
			"\n" + dateUpdate +
			"\n" + news +
			"\n" + erro
		);
	}
}
