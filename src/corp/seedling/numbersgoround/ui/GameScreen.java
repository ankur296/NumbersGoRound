package corp.seedling.numbersgoround.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import ru.biovamp.widget.CircleLayout;
import ru.biovamp.widget.CircleLayout.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import corp.seedling.numbersgoround.R;
import corp.seedling.numbersgoround.data.Data;
import corp.seedling.numbersgoround.utils.AchievementsUnlocked;
import corp.seedling.numbersgoround.utils.Sound;
import corp.seedling.numbersgoround.utils.Utils;


public class GameScreen extends BaseGameActivity {

	private AchievementsUnlocked mAchievementsUnlocked;
	private GoogleApiClient googleApiClient;
	public boolean mConnected = false;
	ArrayList<String> list = new ArrayList<String>();
	FrameLayout frameLayout;
	CustomTextView resetTv , scoreTv;
	CustomTextView centerTv;
	View lastClickedView;
	CircleLayout circleLayout;
	ArrayList<String> exp = new ArrayList<String>();
	boolean startedWithNumber = false;
	String lastTag = "last";
	String currentTag = "current";
	int totalCircles = -1;
	ProgressBar mProgressBar;
	CountDownTimer mCountDownTimer;
	Animation blinkAnimation;
	ScaleAnimation scaleAnim;
	private long gameTime = 0;
	int score = 0;
	int progress = 100;
	ArrayList<String> currentPair = new ArrayList<String>();
	SharedPreferences preferences ;
	private boolean isClockRunning = false;
	Context context;
	PopupWindow timeupPopup;
	long timeLeft = 0 ;
	Animation rotateAnimation, shakeAnimation;
	int circlesKnockedOff = 0;
	private AdView mAdView;


	//statics
	public static int GAME_TIME = 30000;
	private static long BONUS_TIME = 15000;
	private static final int ANIM_TIME = 100;
	public static final int NUMBER_CIRCLE_SIZE_DP = 60;
	private static final int WHAT_START_ROTATION = 1;
	private static final int WHAT_DISPLAY_NUMBERS = 2;
	private static final int WHAT_DISPLAY_CENTER_NUMBER = 3;
	private static final int WHAT_START_TIMER = 4;
	private static final int WHAT_RESET_PROGRESS_COLOR = 5;
	private static final int WHAT_GAME_OVER = 6;
	private static final int WHAT_TIME_UP = 7;
	private static final int WHAT_RESET_GAME = 8;
	private static final int WHAT_START_CLOCK_WIND = 9;

	private static int CORRECT_ANS_PTS = 0;

	private static final int TIME_ROTATION = 1200 + 500; // 3 rotations of 400ms each
	public static final int REQUEST_CODE_LAUNCH_HELP = 7;

	@Override
	public void onSignInSucceeded() {
		System.out.println("Ankur sign in success");
		mConnected = true;
		if(mAchievementsUnlocked.mGoogleApiClient == null && mConnected){
			mAchievementsUnlocked.mGoogleApiClient = getApiClient();
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
		setContentView(R.layout.game_screen);

		findViewsByIds();
		initUI();
		setOnClickListeners();
		resetGame();
		initMisc();

		if (savedInstanceState == null){
			//FRESH START
			makeModeSpecificChanges();
			rotateNumbers(true);
			setUpQuestion(true);
			System.out.println("ankur savedInstanceState = null , mode = " +Data.GAME_MODE +" , prog="+progress);
		}
		else{
			//RESUMED
			score = savedInstanceState.getInt("score", score);
			Data.GAME_MODE = savedInstanceState.getInt("mode", Data.GAME_MODE_EASY);

			makeModeSpecificChanges();
			gameTime = savedInstanceState.getLong("timeLeft", gameTime);
			System.out.println("ankur Data retrieved afterPROCESS KILL tieleft="+gameTime+", mode="+Data.GAME_MODE+", score="+score);
			setUpQuestion(false);
			System.out.println("ankur savedInstanceState != null , mode = " +Data.GAME_MODE +" , prog="+progress);
		}
		scoreTv.setText(getResources().getString(R.string.score)+":"+score);

		//load banner ad
		AdRequest.Builder builder = new AdRequest.Builder();
//		builder.addTestDevice("BE0B283D9DB35079AECC55005262BED2");//TODO: Remove
		this.mAdView.loadAd(builder.build());

		if(!mConnected)  {
			googleApiClient = getApiClient();
		}
		mAchievementsUnlocked = new AchievementsUnlocked(this, googleApiClient);

		if ( 	getIntent().getBooleanExtra("show_ad", true)
				&& GameApp.getAppInstance().interstitialAds.isLoaded() 
				&&  ( (preferences.getInt("count", 0)) % 2 == 0 )
				&&  (preferences.getInt("count", 0) > 2)
				)
			GameApp.getAppInstance().interstitialAds.show();

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong("timeLeft", timeLeft);
		outState.putInt("mode", Data.GAME_MODE);
		outState.putInt("score", score);

		System.out.println("ankur PROCESS KILL.Data saved, tieleft = "+timeLeft + " , mode = "+Data.GAME_MODE+", score="+score);

		//make sure that the user doesnt loses high score
		if (preferences.getInt("high_score", 0) < score)
			preferences.edit().putInt("high_score", score).apply();

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		GameApp.getAppInstance().stopPlaying();

		if (mCountDownTimer != null){
			mCountDownTimer.cancel();
		}

		if (isClockRunning == true)
			Sound.pauseSound(Sound.SOUND_CLOCK_START);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mCountDownTimer != null)
			startTimer(timeLeft);

		GameApp.getAppInstance().resumePlaying();

		if (isClockRunning == true){
			Sound.playSound(Sound.SOUND_CLOCK_START);
		}
		if(mAchievementsUnlocked.mGoogleApiClient == null && mConnected){
			mAchievementsUnlocked.mGoogleApiClient = getApiClient();
		}
	}

	private void rotateNumbers(boolean freshStart) {
		if (freshStart){
			handler.sendEmptyMessageDelayed(WHAT_START_ROTATION, 100);
			handler.sendEmptyMessageDelayed(WHAT_START_CLOCK_WIND, 1000);
		}
		else{
			handler.sendEmptyMessageDelayed(WHAT_START_ROTATION, 100);
			handler.sendEmptyMessageDelayed(WHAT_START_CLOCK_WIND, 100);

		}
		handler.sendEmptyMessageDelayed(WHAT_START_TIMER, TIME_ROTATION);
	}


	private final ClickListener clickListener = new ClickListener(this);
	private static class ClickListener implements OnClickListener{

		private final WeakReference<GameScreen> actInstance ;

		public ClickListener(GameScreen activity){
			actInstance = new WeakReference<GameScreen>(activity);
		}

		@Override
		public void onClick(View view) {
			GameScreen gameScreen = actInstance.get();

			if (view.getId() == gameScreen.resetTv.getId()){
				Sound.playSound(Sound.SOUND_GENERIC_PRESS);
				gameScreen.reArrangeQuestion(false);
			}

			else{
				Sound.playSound(Sound.SOUND_TOUCH);

				if (!gameScreen.startedWithNumber){
					//CANT PROCEED
					if (view.getTag().equals("number")){

						gameScreen.currentTag = view.getTag().toString();
						gameScreen.startedWithNumber = true;
					}
					else{
						Toast.makeText(gameScreen, "Click on Number followed by Operator !", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					gameScreen.currentTag = view.getTag().toString();
				}

				//ALL checks done!! Lets start!
				if( (gameScreen.startedWithNumber) && (!gameScreen.lastTag.equalsIgnoreCase(gameScreen.currentTag)) ){

					//check for DBZ error
					if (
							((CustomTextView)view).getText().toString().equalsIgnoreCase("0") 
							&& 	((CustomTextView)gameScreen.lastClickedView).getText().toString().equalsIgnoreCase("÷")
							)  {
						//Reset the current tag and return
						Toast.makeText(gameScreen, "Can't divide by ZERO !", Toast.LENGTH_LONG).show();
						gameScreen.currentTag = gameScreen.lastTag;
						return;
					}

					gameScreen.lastClickedView = view;
					gameScreen.exp.add( ""+  ((CustomTextView)view).getText() );

					if (gameScreen.exp.size() == 3){

						int interimResult =  gameScreen.evaluateExp(gameScreen.exp) ;

						if(gameScreen.circlesKnockedOff == gameScreen.totalCircles - 1){

							if (interimResult == Integer.parseInt(gameScreen.centerTv.getText().toString()) ){

								//CORRECT ANSWER !!
								Sound.playSound(Sound.SOUND_TOUCH);
								gameScreen.updateScore();
								gameScreen.checkAchievements();

								if (gameScreen.timeLeft + BONUS_TIME < GAME_TIME)
									gameScreen.gameTime = gameScreen.timeLeft + BONUS_TIME ;
								else
									gameScreen.gameTime = GAME_TIME ;

								//pause timer until rotation ends
								gameScreen.mCountDownTimer.cancel();

								gameScreen.reArrangeQuestion(true);
								return;
							}
							else{
								//WRONG ANSWER
								//shake central view
								gameScreen.shakeCenter();
								Sound.playSound(Sound.SOUND_WRONG);
								//reset Q
								gameScreen.handler.sendEmptyMessageDelayed(WHAT_RESET_GAME, 1000);
							}
						}
						gameScreen.exp.clear();
						gameScreen.exp.add( String.valueOf(interimResult));

						((CustomTextView)view).setText(gameScreen.exp.get(0));
						gameScreen.exp.clear();

						gameScreen.startedWithNumber = false;
						gameScreen.lastTag = "last";
						gameScreen.currentTag = "current";  
					}
					else{
						//remove the view
						view.setVisibility(View.GONE);
						//						view.setTag(null);
						gameScreen.circlesKnockedOff++;
						System.out.println("ankur circlesKnockedOff = " +gameScreen.circlesKnockedOff);
					}
					gameScreen.lastTag = gameScreen.currentTag;
				}
				else{
				}
			}
		}
	}

	private void checkAchievements(){
		if(mConnected){

			if( score >= 10 && score < 15)
				mAchievementsUnlocked.case10();

			else if ( score >= 50 && score < 55)
				mAchievementsUnlocked.case50();

			else if ( score >= 100 && score < 105)
				mAchievementsUnlocked.case100();

			else if ( score >= 150 && score < 155)
				mAchievementsUnlocked.case150();

			else if ( score >= 200 && score < 205)
				mAchievementsUnlocked.case200();
		}
	}	

	private void updateScore(){
		score += CORRECT_ANS_PTS;
		scoreTv.setText(getResources().getString(R.string.score)+":"+score);
	}	

	private int evaluateExp(ArrayList<String> input){

		System.out.println("ankur interm exp = " + exp);
		int result = -1;

		if ( input.get(1).equalsIgnoreCase("+") )
			result = Integer.parseInt("" + input.get(0)) + Integer.parseInt("" + input.get(2));

		else if ( input.get(1).equalsIgnoreCase("-") )
			result = Integer.parseInt("" + input.get(0)) - Integer.parseInt("" + input.get(2));

		else if ( input.get(1).equalsIgnoreCase("x") )
			result = Integer.parseInt(""+input.get(0)) * Integer.parseInt(""+input.get(2));

		else if ( input.get(1).equalsIgnoreCase("÷") )
			result =  (int) (Integer.parseInt(""+input.get(0)) / Integer.parseInt(""+input.get(2)) );

		System.out.println("ankur interim result = " + result);

		return result;
	}


	private void startTimer(final long startFrom) {

		System.out.println("ankur starttimer enter");
		mProgressBar.setProgress((int) ( 100*   (float)startFrom / GAME_TIME ));
		//		mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.rectangular_progress_bar));

		if (mCountDownTimer != null){

			mCountDownTimer.cancel();
			mCountDownTimer = null;

			mCountDownTimer=new CountDownTimer(startFrom,1) {

				@Override
				public void onTick(long millisUntilFinished) {
					progress =  (int) ( 100*   (float) ( millisUntilFinished) / GAME_TIME ) ;

					if (progress < 25){
						if ( mProgressBar.getAnimation() == null)
							mProgressBar.startAnimation(blinkAnimation);

						if (isClockRunning == false){
							mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circle_progress_foreground_red));
							Sound.playSound(Sound.SOUND_CLOCK_START);
							isClockRunning = true;
						}
						GameApp.getAppInstance().loadAd();
					}
					else{
						if ( mProgressBar.getAnimation() != null)
							mProgressBar.clearAnimation();

						if (isClockRunning == true){
							mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circle_progress_foreground));
							Sound.pauseSound(Sound.SOUND_CLOCK_START);
							isClockRunning = false;
						}
					}
					timeLeft = millisUntilFinished;
					mProgressBar.setProgress(progress);
				}

				@Override
				public void onFinish() {
					progress = 0;
					mProgressBar.setProgress(progress);
					mProgressBar.clearAnimation();
					handler.sendEmptyMessage(WHAT_TIME_UP);
					handler.sendEmptyMessageDelayed(WHAT_GAME_OVER, 1500);
					isClockRunning = false;
					timeLeft = 0;
					mCountDownTimer = null;
				}
			};
			mCountDownTimer.start();
		}
		else{
			mCountDownTimer=new CountDownTimer(startFrom,1) {
				@Override
				public void onTick(long millisUntilFinished) {

					progress =  (int) ( 100*   (float)millisUntilFinished / GAME_TIME ) ;
					if (progress < 25){

						if ( mProgressBar.getAnimation() == null)
							mProgressBar.startAnimation(blinkAnimation);

						if (isClockRunning == false){
							mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circle_progress_foreground_red));
							Sound.playSound(Sound.SOUND_CLOCK_START);
							isClockRunning = true;
						}
						GameApp.getAppInstance().loadAd();
					}
					else{
						if ( mProgressBar.getAnimation() != null)
							mProgressBar.clearAnimation();

						if (isClockRunning == true){
							mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circle_progress_foreground));

							Sound.pauseSound(Sound.SOUND_CLOCK_START);
							isClockRunning = false;
						}
					}
					mProgressBar.setProgress(progress);
					timeLeft = millisUntilFinished;
				}

				@Override
				public void onFinish() {
					progress = 0;
					mProgressBar.setProgress(progress);
					mProgressBar.clearAnimation();
					handler.sendEmptyMessage(WHAT_TIME_UP);
					handler.sendEmptyMessageDelayed(WHAT_GAME_OVER, 1500);
					isClockRunning = false;
					timeLeft = 0;
					mCountDownTimer = null;
				}
			};
			mCountDownTimer.start();
		}
	}


	private final MyHandler handler = new MyHandler(this);
	private static class MyHandler extends Handler{
		private final WeakReference<GameScreen> actInstance;

		public MyHandler(GameScreen activity){
			actInstance = new WeakReference<GameScreen>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			GameScreen gameScreen = actInstance.get();

			switch (msg.what) {

			case WHAT_START_ROTATION:
				gameScreen.rotateAnimation = AnimationUtils.loadAnimation(gameScreen, R.anim.rotate_fast_inverse);
				gameScreen.circleLayout.startAnimation(gameScreen.rotateAnimation);
				break;

			case WHAT_START_CLOCK_WIND:
				Sound.playSound(Sound.SOUND_CLOCK_WIND);
				break;

			case WHAT_DISPLAY_NUMBERS:
				for (int i = 0 ; i < gameScreen.totalCircles ; i++){
					CustomTextView child = (CustomTextView)gameScreen.circleLayout.getChildAt(i);
					child.setText( "" + gameScreen.list.get(i) );

					child.setOriginalValue(gameScreen.list.get(i));
				}
				break;

			case WHAT_DISPLAY_CENTER_NUMBER:
				System.out.println("ankur Rx new center no: " + msg.obj.toString() );
				gameScreen.centerTv.setText( "" + msg.obj.toString() );
				gameScreen.startTimer(gameScreen.gameTime);

				//start anim
				gameScreen.centerTv.startAnimation(gameScreen.scaleAnim);
				break;

			case WHAT_RESET_PROGRESS_COLOR:
				gameScreen.mProgressBar.setProgressDrawable(gameScreen.getResources().getDrawable(R.drawable.circle_progress_foreground));
				break;

			case WHAT_RESET_GAME:
				gameScreen.reArrangeQuestion(false);
				break;

			case WHAT_START_TIMER:
				gameScreen.startTimer(gameScreen.gameTime);
				break;

			case WHAT_GAME_OVER:

				gameScreen.preferences.edit().putInt("score", gameScreen.score).apply();
				
				int highScore = gameScreen.preferences.getInt("high_score", 0);
				
				if ( highScore < gameScreen.score){
					gameScreen.preferences.edit().putInt("high_score", gameScreen.score).apply();
					highScore = gameScreen.score;
				}
				gameScreen.timeupPopup.dismiss();

				Intent intent = new Intent(gameScreen, GameOverScreen.class);
				intent.putExtra("score", gameScreen.score);
				intent.putExtra("high_score", highScore);
				
				//				intent.putExtra("list", TextUtils.join(", ", gameScreen.list));
				gameScreen.startActivity(intent);
				gameScreen.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

				//game over..checkin score
				if(gameScreen.mConnected){
					Games.Leaderboards.submitScore(gameScreen.googleApiClient, 
							gameScreen.getResources().getString(R.string.leaderboard_einsteins), gameScreen.score);
				}

				gameScreen.finish();

				break;

			case WHAT_TIME_UP:

				Sound.pauseSound(Sound.SOUND_CLOCK_START);
				Sound.playSound(Sound.SOUND_TIME_UP);
				ScaleAnimation scale2 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
				scale2.setFillAfter(true);
				scale2.setDuration(500);

				for(int i = 0 ; i < gameScreen.circleLayout.getChildCount() ; i++){

					CustomTextView textView = (CustomTextView)gameScreen.circleLayout.getChildAt(i);

					textView.startAnimation(scale2);
				}

				gameScreen.centerTv.startAnimation(scale2);
				//show timeup popup
				LayoutInflater inflater = (LayoutInflater)
						gameScreen.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				gameScreen.timeupPopup = new PopupWindow(
						inflater.inflate(R.layout.popup_timeup, null, false), 
						LayoutParams.WRAP_CONTENT,  
						LayoutParams.WRAP_CONTENT, 
						true);

				gameScreen.timeupPopup.setAnimationStyle(R.style.Animation);
				gameScreen.timeupPopup.showAtLocation(gameScreen.findViewById(R.id.lay_circle), Gravity.CENTER, 0, 0); 


				break;

			default:
				break;
			}
		}
	}

	private void resetGame(){
		currentTag = "current";
		lastTag = "last";
		startedWithNumber = false;
		exp.clear();
		progress = 100;
		circlesKnockedOff = 0;
		score = 0;
		isClockRunning = false;
		mProgressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circle_progress_foreground));

	}

	private void removeViews(){
		for (int i = 0 ; i < totalCircles ; i++){

			CustomTextView child = (CustomTextView)circleLayout.getChildAt(i);
			child.setBackground(null);
			child.setOnClickListener(null);
		}
		circleLayout.removeAllViews();

	}

	private void setUpQuestion(boolean freshStart){

		if (freshStart){
			list = Data.getData();

			String joined = TextUtils.join(", ", list);
			preferences.edit().putString("list", joined).apply();

			for (String ss : list)
				System.out.println("ankur fresh list = " +ss);
		}
		else{
			String s =  preferences.getString("list", "");
			list = new ArrayList<String>(Arrays.asList(s.split(",")));

			for (int i = 0; i < list.size(); i++){
				list.set(i, list.get(i).replaceAll("\\s","") );
			}
		}
		Utils.prepColorList();

		centerTv.setBackgroundResource(Utils.colorList.get(totalCircles));
		centerTv.setText( "O");
		centerTv.setTextSize(25);

		//display central number after rotation stops
		handler.sendMessageDelayed(Message.obtain(handler, WHAT_DISPLAY_CENTER_NUMBER, list.get(totalCircles)), TIME_ROTATION);
		list.remove(totalCircles);
		Collections.shuffle(list);

		for (int i = 0 ; i < totalCircles ; i++){

			CustomTextView child = new CustomTextView(this);
			child.setBackgroundResource(Utils.colorList.get(i));
			child.setText( "O" );

			if ( isOperand(list.get(i)) ){
				child.setTag("operator");
			}
			else{
				child.setTag("number");
			}
			child.setOnClickListener(clickListener);
			LayoutParams layoutParams = new LayoutParams(getSizeInPixels(NUMBER_CIRCLE_SIZE_DP), getSizeInPixels(NUMBER_CIRCLE_SIZE_DP));
			circleLayout.addView(child, layoutParams);
		}

		//display numbers after rotation starts
		handler.sendEmptyMessageDelayed(WHAT_DISPLAY_NUMBERS, 150);
	}

	private void shakeCenter(){
		shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
		centerTv.startAnimation(shakeAnimation);
	}	

	private void reArrangeQuestion(boolean newGame){

		currentTag = "current";
		lastTag = "last";
		startedWithNumber = false;
		exp.clear();
		circlesKnockedOff = 0;

		if (newGame){
			System.out.println("ankur setup new Q");
			list.clear();
			list = Data.getData();

			//save the list
			String joined = TextUtils.join(", ", list);
			preferences.edit().putString("list", joined).apply();

			Utils.prepColorList();
			centerTv.setBackgroundResource(Utils.colorList.get(totalCircles));
			centerTv.setText( "O" );
			rotateNumbers(false);

			//turn progress back to white when rotation stops
			if (progress < 25 ){
				handler.sendEmptyMessage(WHAT_RESET_PROGRESS_COLOR);
			}else{
				System.out.println("ankur prog>25 so no need to change color");
			}
			//display central number after rotation stops
			handler.sendMessageDelayed(Message.obtain(handler, WHAT_DISPLAY_CENTER_NUMBER, list.get(totalCircles)), TIME_ROTATION);

			list.remove(totalCircles);
		}

		Collections.shuffle(list);

		for (int i = 0 ; i < totalCircles ; i++){

			CustomTextView child = (CustomTextView)circleLayout.getChildAt(i);
			//			child.setBackgroundResource(Utils.colorList.get(i));
			child.setText( "" + list.get(i) );

			if ( isOperand(list.get(i)) )
				child.setTag("operator");
			else
				child.setTag("number");

			child.setVisibility(View.VISIBLE);
		}

		//display numbers after rotation starts
		handler.sendEmptyMessageDelayed(WHAT_DISPLAY_NUMBERS, 150);
	}


	private void initMisc(){
		blinkAnimation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
		blinkAnimation.setDuration(300); // duration - half a second
		blinkAnimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
		blinkAnimation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
		blinkAnimation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

		scaleAnim = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
		scaleAnim.setRepeatCount(Animation.INFINITE);
		scaleAnim.setRepeatMode(Animation.REVERSE);
		scaleAnim.setFillAfter(true);
		scaleAnim.setDuration(1000);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	private void makeModeSpecificChanges(){
		//config changes based on mode
		if (Data.GAME_MODE == Data.GAME_MODE_HARD){
			totalCircles = 9;
			GAME_TIME = 60000;
			BONUS_TIME = 45000;
			CORRECT_ANS_PTS = 3;
		}
		else if (Data.GAME_MODE == Data.GAME_MODE_CRAZY){
			totalCircles = 9; 
			GAME_TIME = 90000;
			BONUS_TIME = 60000;
			CORRECT_ANS_PTS = 5;
		}
		else{
			totalCircles = 7;
			GAME_TIME = 30000;
			BONUS_TIME = 15000;
			CORRECT_ANS_PTS = 1;
		}

		gameTime = GAME_TIME;
	}

	@Override
	protected void onDestroy() {

		if (handler != null){
			handler.removeCallbacksAndMessages(null);
		}

		if (mCountDownTimer != null){
			mCountDownTimer.cancel();
			mCountDownTimer = null;
		}
		if (mProgressBar != null){
			Drawable cellDrawable = mProgressBar.getBackground();
			if (cellDrawable != null){
				cellDrawable.setCallback(null);
				cellDrawable = null;
			}
			mProgressBar.setBackground(null);
			mProgressBar = null;
		}

		Drawable drawable = centerTv.getBackground();

		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		centerTv.setAnimation(null);
		centerTv = null;

		drawable = resetTv.getBackground();

		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		resetTv = null;

		drawable = scoreTv.getBackground();

		if (drawable != null ){
			drawable.setCallback(null);
			drawable = null;
		}
		scoreTv = null;

		for (int i = 0 ; i <  totalCircles ; i++){

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
	public void onBackPressed() {
		startActivity(new Intent(this, StartingScreen.class));
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		finish();
		super.onBackPressed();
	}

	private void initUI(){
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

		circleLayout =	new CircleLayout(this);
		circleLayout.setInnerRadius((displayMetrics.widthPixels / 2) - 0 - getSizeInPixels(NUMBER_CIRCLE_SIZE_DP));
		circleLayout.setInnerCircleColor(Color.parseColor("#5b717c"));
		frameLayout.addView(circleLayout);
	}

	private boolean isOperand(String s){
		if (s.equalsIgnoreCase("+") || s.equalsIgnoreCase("-") || s.equalsIgnoreCase("x") || s.equalsIgnoreCase("÷") )
			return true;
		else
			return false;
	}

	private void setOnClickListeners() {
		resetTv.setOnClickListener(clickListener);
	}	

	private void findViewsByIds() {
		mProgressBar = (ProgressBar)findViewById(R.id.circle_progress_bar);
		frameLayout = (FrameLayout)findViewById(R.id.lay_circle);
		centerTv = (CustomTextView) findViewById(R.id.tv_center);
		resetTv = (CustomTextView) findViewById(R.id.tv_reset);
		scoreTv = (CustomTextView) findViewById(R.id.tv_score_game);
		this.mAdView = ((AdView)findViewById(R.id.google_ad_main));
	}

	public int getSizeInPixels(int dp){
		return (int) ( (dp * getResources().getDisplayMetrics().density)  + 0.5f) ; 
	}
}
