package corp.seedling.numbersgoround.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;

import corp.seedling.numbersgoround.R;

import corp.seedling.numbersgoround.data.Data;
import corp.seedling.numbersgoround.utils.Sound;

public class ModeSelectionScreen extends Activity{

	CustomTextView easy , hard, crazy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mode_screen);
		easy = (CustomTextView) findViewById(R.id.tv_mode_easy);
		hard = (CustomTextView) findViewById(R.id.tv_mode_hard);
		crazy = (CustomTextView) findViewById(R.id.tv_mode_crazy);

		easy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Data.GAME_MODE = Data.GAME_MODE_EASY;
				Sound.playSound(Sound.SOUND_GENERIC_PRESS);	

				Intent intent = new Intent(ModeSelectionScreen.this, GameScreen.class);
				
				System.out.println("ankur mode screen..show_ad value = "+ getIntent().getBooleanExtra("show_ad", false));
				if (getIntent().getBooleanExtra("show_ad", false))
					intent.putExtra("show_ad", true);
				
				else
					intent.putExtra("show_ad", false);
				
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				finish();
			}
		});

		hard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Data.GAME_MODE = Data.GAME_MODE_HARD;
				Sound.playSound(Sound.SOUND_GENERIC_PRESS);

				Intent intent = new Intent(ModeSelectionScreen.this, GameScreen.class);
				if (getIntent().getBooleanExtra("show_ad", false))
					intent.putExtra("show_ad", true);
				else
					intent.putExtra("show_ad", false);
				
				startActivity(intent);

				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				finish();
			}
		});

		crazy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Data.GAME_MODE = Data.GAME_MODE_CRAZY;
				Sound.playSound(Sound.SOUND_GENERIC_PRESS);

				Intent intent = new Intent(ModeSelectionScreen.this, GameScreen.class);
				if (getIntent().getBooleanExtra("show_ad", false))
					intent.putExtra("show_ad", true);
				else
					intent.putExtra("show_ad", false);
				
				startActivity(intent);

				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		Drawable drawable = easy.getBackground();
		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		easy.setBackground(null);
		easy.setOnClickListener(null);
		easy = null;

		drawable = hard.getBackground();
		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		hard.setBackground(null);
		hard.setOnClickListener(null);
		hard = null;
		
		drawable = crazy.getBackground();
		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		crazy.setBackground(null);
		crazy.setOnClickListener(null);
		crazy = null;
		
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		GameApp.getAppInstance().stopPlaying();
	}

	@Override
	protected void onResume() {
		super.onResume();
		GameApp.getAppInstance().resumePlaying();
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, StartingScreen.class));
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		finish();
		super.onBackPressed();
	}
}
