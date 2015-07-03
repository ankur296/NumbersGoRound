package corp.seedling.numbersgoround.ui;

import java.lang.ref.WeakReference;

import ru.biovamp.widget.CircleLayout;
import ru.biovamp.widget.CircleLayout.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import corp.seedling.numbersgoround.R;
import corp.seedling.numbersgoround.utils.Sound;
import corp.seedling.numbersgoround.utils.Utils;

public class StartingScreen extends BaseGameActivity{

	FrameLayout frameLayout;
	CustomTextViewBar resetTv, modeTv;
	CustomTextView play;
	CircleLayout circleLayout;
	Animation rotateAnimation;
	SharedPreferences preferences;

	public static final int MENU_ITEM_CIRCLE_SIZE_DP = 60;
	public static final int TOTAL_MENU_ITEMS = 8;

	private GoogleApiClient googleApiClient;
	public boolean mConnected = false;

	@Override
	public void onSignInSucceeded() {
		System.out.println("Ankur sign in success");
		mConnected = true;
		if( (googleApiClient == null) && mConnected){
			googleApiClient = getApiClient();
		}
	}

	@Override
	public void onSignInFailed() {
		System.out.println("Ankur sign in fail");
		mConnected = false;
		googleApiClient = null;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starting_screen);

		findViewsByIds();
		initMisc();
		initUI();
		//		rotateMenu();
		setOnClickListeners();

		if(!mConnected)  {
			googleApiClient = getApiClient();
		}

	}

	private void initMisc() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	private void initUI() {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

		circleLayout =	new CircleLayout(this);
		circleLayout.setInnerRadius((displayMetrics.widthPixels / 2) - 0 - getSizeInPixels(MENU_ITEM_CIRCLE_SIZE_DP));

		for (int i = 0 ; i < TOTAL_MENU_ITEMS ; i++){

			CustomTextView child = new CustomTextView(this);
			child.setId(i);
			setBGforChild(child , i);
			child.setOnClickListener(listener);
			LayoutParams layoutParams = new LayoutParams(getSizeInPixels(MENU_ITEM_CIRCLE_SIZE_DP), getSizeInPixels(MENU_ITEM_CIRCLE_SIZE_DP));
			circleLayout.addView(child, layoutParams);
		}
		frameLayout.addView(circleLayout);
	}

	private void setBGforChild(CustomTextView textView, int i) {
		switch (i) {
		case 0:
			textView.setBackground(getResources().getDrawable(R.drawable.circle_help));
			break;

		case 1:
			if (preferences.getBoolean("music", true) == true)
				textView.setBackground(getResources().getDrawable(R.drawable.circle_settings_music));
			else
				textView.setBackground(getResources().getDrawable(R.drawable.circle_settings_music_off));
			break;

		case 2:
			textView.setBackground(getResources().getDrawable(R.drawable.circle_blue_purple));
			textView.setText("SFX");

			if (preferences.getBoolean("sfx", true) == false){
				textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}
			break;
		case 3:
			textView.setBackground(getResources().getDrawable(R.drawable.circle_share));
			break;
		case 4:
			textView.setBackground(getResources().getDrawable(R.drawable.circle_rate));
			break;
		case 5:
			textView.setBackground(getResources().getDrawable(R.drawable.circle_leaderboard));
			break;
		case 6:
			textView.setBackground(getResources().getDrawable(R.drawable.circle_achievements));
			break;
		case 7:
			textView.setBackground(getResources().getDrawable(R.drawable.circle_crosspromo));
			break;
		default:
			break;
		}		
	}

	private final MyClickListener listener = new MyClickListener(this);
	private static class MyClickListener implements OnClickListener{
		private final WeakReference<StartingScreen> actInstance;


		public MyClickListener(StartingScreen activity) {
			actInstance = new WeakReference<StartingScreen>(activity);
		}

		@Override
		public void onClick(View v) {

			StartingScreen gameScreen = actInstance.get();

			Sound.playSound(Sound.SOUND_GENERIC_PRESS);

			if (v.getId() == gameScreen.play.getId()){

				if (gameScreen.preferences.getBoolean("first_launch", true) == true){
					gameScreen.preferences.edit().putBoolean("first_launch", false).apply();

					Intent intent = new Intent(gameScreen, HelpScreen.class);
					intent.putExtra("launched_over_play", true);
					gameScreen.startActivity(intent);
					gameScreen.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
					gameScreen.finish();
				}
				else{

					Intent intent = new Intent(gameScreen, ModeSelectionScreen.class);
					gameScreen.startActivity(intent);
					gameScreen.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
					gameScreen.finish();
				}
			}
			//id = 0: Help
			else if (v.getId() == 0){
				Intent intent = new Intent(gameScreen, HelpScreen.class);
				gameScreen.startActivity(intent);
				gameScreen.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				gameScreen.finish();
			}


			//id = 1: music
			else if (v.getId() == 1){
				if (gameScreen.preferences.getBoolean("music", true) == true ){

					GameApp.getAppInstance().stopPlaying();
					v.setBackground(gameScreen.getResources().getDrawable(R.drawable.circle_settings_music_off));
					gameScreen.preferences.edit().putBoolean("music", false).apply();

				}
				else if (gameScreen.preferences.getBoolean("music", true) == false ){

					v.setBackground(gameScreen.getResources().getDrawable(R.drawable.circle_settings_music));
					gameScreen.preferences.edit().putBoolean("music", true).apply();
					GameApp.getAppInstance().resumePlaying();

				} 
			}

			//id = 2: SFX
			else if (v.getId() == 2){

				if (gameScreen.preferences.getBoolean("sfx", true) == true ){
					gameScreen.preferences.edit().putBoolean("sfx", false).apply();
					Sound.sfx = false;
					TextView someTextView = (TextView) v;
					someTextView.setPaintFlags(someTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				}
				else if (gameScreen.preferences.getBoolean("sfx", true) == false ){
					Sound.sfx = true;
					gameScreen.preferences.edit().putBoolean("sfx", true).apply();
					TextView someTextView = (TextView) v;
					someTextView.setPaintFlags(someTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
				} 
			}

			//id = 3: Share
			else if (v.getId() == 3){
				gameScreen.shareit();
			}

			//id = 4: Rate
			else if (v.getId() == 4){
				gameScreen.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.SEVEN_LETTERS_URL)));
			}

			//id = 5: Top Scorers
			else if (v.getId() == 5){
				if (gameScreen.mConnected)
				{
					gameScreen.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameScreen.googleApiClient, gameScreen.getResources().getString(R.string.leaderboard_einsteins)),11);
					return; 
				}
				gameScreen.beginUserInitiatedSignIn();
			}

			//id = 6: Achievements
			else if (v.getId() == 6){
				if (gameScreen.mConnected)
				{
					gameScreen.startActivityForResult(Games.Achievements.getAchievementsIntent(gameScreen.googleApiClient), 10 );
					return; 
				}
				gameScreen.beginUserInitiatedSignIn();
			}

			//id = 7: Cross Promo
			else if (v.getId() == 7){
				gameScreen.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.PUBLISHER_URL)));
			}
		}
	}

	@Override
	protected void onDestroy() {
		Drawable drawable = play.getBackground();

		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		play.setBackground(null);
		play.setOnClickListener(null);
		play = null;

		for (int i = 0 ; i < TOTAL_MENU_ITEMS ; i++){

			CustomTextView child = (CustomTextView)circleLayout.getChildAt(i);
			drawable = child.getBackground();
			if (drawable != null){
				drawable.setCallback(null);
				drawable = null;
			}
			child.setBackground(null);
			child.setOnClickListener(null);
			child = null;
		}

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

	private void shareit(){ 
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		emailIntent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.share_text) +Utils.NUMBERS_GO_ROUND_URL_HTTP);
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Share!"));
	}


	private void rotateMenu() {
		rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_slow);
		circleLayout.startAnimation(rotateAnimation);
	}

	private void setOnClickListeners() {
		play.setOnClickListener(listener);
	}	

	private void findViewsByIds() {
		frameLayout = (FrameLayout)findViewById(R.id.lay_circle_start);
		play = (CustomTextView) findViewById(R.id.tv_play_start);
	}

	public int getSizeInPixels(int dp){
		return (int) ( (dp * getResources().getDisplayMetrics().density)  + 0.5f) ; 
	}
}
