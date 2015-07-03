package corp.seedling.numbersgoround.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import corp.seedling.numbersgoround.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import corp.seedling.numbersgoround.data.Data;
import corp.seedling.numbersgoround.utils.Sound;
import corp.seedling.numbersgoround.utils.Utils;


public class GameOverScreen extends BaseGameActivity{

	LinearLayout layWord1, layWord2;
	CustomTextView tvRestart , tvScore, tvShare , tvHighScore, tvLeader, tvAchieve;
	private static final int ANIM_TIME = 100;
	int score;
	SharedPreferences preferences;
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
		setContentView(R.layout.game_over_screen);

		initUI();  
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		Bundle bundle = getIntent().getExtras();
		score = bundle.getInt("score", 0);
		showAnswers(preferences.getString("list", ""));

		setClickListeners();
		//set stats values
		tvScore.setText(""+score);
		tvHighScore.setText(""+bundle.getInt("high_score", 0));

		preferences.edit().putInt("count", preferences.getInt("count", 0) + 1 ).apply();
		
		System.out.println("ankur game over screen.. new count = " + preferences.getInt("count", 0));
		

		if(!mConnected)  {
			googleApiClient = getApiClient();
		}

	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, StartingScreen.class));
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		finish();
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		GameApp.getAppInstance().stopPlaying();
		//		Sound.unloadSound();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//		Sound.initSound();
		GameApp.getAppInstance().resumePlaying();
	}	

	private void setClickListeners() {
		tvRestart.setOnClickListener(clickListener);
		tvLeader.setOnClickListener(clickListener);
		tvAchieve.setOnClickListener(clickListener);
	}

	private void initUI() {
		tvRestart = (CustomTextView)findViewById(R.id.tv_restart);
		tvScore = (CustomTextView)findViewById(R.id.tv_score);
		tvHighScore = (CustomTextView)findViewById(R.id.tv_high_score);
		tvShare = (CustomTextView)findViewById(R.id.tv_dict);
		tvLeader = (CustomTextView)findViewById(R.id.tv_leaderboard);
		tvAchieve = (CustomTextView)findViewById(R.id.tv_achieve);
	}

	@Override
	protected void onDestroy() {

		tvRestart.setBackground(null);
		tvRestart.setOnClickListener(null);
		tvRestart= null;

		tvScore.setBackground(null);
		tvScore.setOnClickListener(null);
		tvScore= null;

		tvHighScore.setBackground(null);
		tvHighScore.setOnClickListener(null);
		tvHighScore= null;

		tvShare.setBackground(null);
		tvShare.setOnClickListener(null);
		tvShare = null;

		tvLeader.setBackground(null);
		tvLeader.setOnClickListener(null);
		tvLeader= null;

		tvAchieve.setBackground(null);
		tvAchieve.setOnClickListener(null);
		tvAchieve= null;

		super.onDestroy();
	}

	private void showAnswers(String list) {

		( (TextView)findViewById(R.id.tv_answer) ).setText(evaluateExpression(list));
		Sound.playSound(Sound.SOUND_SCORE);
	}

	private String evaluateExpression(String expList) {

		System.out.println("ankur String Rx frm gamescreen = " +expList);
		ArrayList<String> exp = new ArrayList<String>(Arrays.asList(expList.split(",")));

		for (String s : exp)
			System.out.println("ankur list Rx frm gamescreen = " +s);

		ArrayList<String> resultExp = new ArrayList<String>();

		int result1 = 0;
		if (Data.GAME_MODE == Data.GAME_MODE_CRAZY){

			String oper = exp.get(1).replaceAll("\\s","");
			
			if ( oper.equalsIgnoreCase("-") ){
				result1 = Integer.parseInt(exp.get(0).replaceAll("\\s","") ) - Integer.parseInt(exp.get(2).replaceAll("\\s",""));
				resultExp.add("" + exp.get(0).replaceAll("\\s","") + " - " + exp.get(2).replaceAll("\\s","") + " = " + result1);
			}

			else if ( oper.equalsIgnoreCase("+") ){
				result1 = Integer.parseInt(exp.get(0).replaceAll("\\s","") ) + Integer.parseInt(exp.get(2).replaceAll("\\s",""));
				resultExp.add("" + exp.get(0).replaceAll("\\s","") + " + " + exp.get(2).replaceAll("\\s","") + " = " + result1);
			}

			else if ( oper.equalsIgnoreCase("x") ){
				result1 = Integer.parseInt(exp.get(0).replaceAll("\\s","") ) * Integer.parseInt(exp.get(2).replaceAll("\\s",""));
				resultExp.add("" + exp.get(0).replaceAll("\\s","") + " x " + exp.get(2).replaceAll("\\s","") + " = " + result1);
			}

			else if ( oper.equalsIgnoreCase("÷") ){
				result1 =  (int) (Integer.parseInt(exp.get(0).replaceAll("\\s","") ) / Integer.parseInt(exp.get(2).replaceAll("\\s","")));
				resultExp.add("" + exp.get(0).replaceAll("\\s","") + " ÷ " + exp.get(2).replaceAll("\\s","") + " = " + result1);
			}
			
		}
		else{
			result1 = Integer.parseInt(exp.get(0).replaceAll("\\s","") ) - Integer.parseInt(exp.get(2).replaceAll("\\s",""));
			resultExp.add("" + exp.get(0).replaceAll("\\s","") + " - " + exp.get(2).replaceAll("\\s","") + " = " + result1);
		}

		//1st operator was "-" which is already handled

		//handle 2nd operator
		int result2 = 0;
		String oper = exp.get(3).replaceAll("\\s","");
		
		if ( oper.equalsIgnoreCase("-") ){
			result2 = result1 - Integer.parseInt(exp.get(4).replaceAll("\\s",""));
			resultExp.add("" + result1 + " - " + exp.get(4).replaceAll("\\s","") + " = " + result2);
		}

		else if ( oper.equalsIgnoreCase("+") ){
			result2 = result1 + Integer.parseInt(exp.get(4).replaceAll("\\s",""));
			resultExp.add("" + result1 + " + " + exp.get(4).replaceAll("\\s","") + " = " + result2);
		}

		else if ( oper.equalsIgnoreCase("x") ){
			result2 = result1 *  Integer.parseInt(exp.get(4).replaceAll("\\s",""));
			resultExp.add("" + result1 + " x " + exp.get(4).replaceAll("\\s","") + " = " + result2);
		}

		else if ( oper.equalsIgnoreCase("÷") ){
			result2 = (int) ( result1 / Integer.parseInt(exp.get(4).replaceAll("\\s","")) ) ;
			resultExp.add("" + result1 + " ÷ " + exp.get(4).replaceAll("\\s","") + " = " + result2);
		}

		//handle 3rd operator
		int result3 = 0;
		oper = exp.get(5).replaceAll("\\s","");

		if ( oper.equalsIgnoreCase("-") ){
			result3 = result2 - Integer.parseInt(exp.get(6).replaceAll("\\s",""));
			resultExp.add("" + result2 + " - " + exp.get(6).replaceAll("\\s","") + " = " + result3);
		}

		else if ( oper.equalsIgnoreCase("+") ){
			result3 = result2 + Integer.parseInt(exp.get(6).replaceAll("\\s",""));
			resultExp.add("" + result2 + " + " + exp.get(6).replaceAll("\\s","") + " = " + result3);
		}

		else if ( oper.equalsIgnoreCase("x") ){
			result3 = result2 *  Integer.parseInt(exp.get(6).replaceAll("\\s",""));
			resultExp.add("" + result2 + " x " + exp.get(6).replaceAll("\\s","") + " = " + result3);
		}

		else if ( oper.equalsIgnoreCase("÷") ){
			result3 = (int) ( result2 / Integer.parseInt(exp.get(6).replaceAll("\\s","")) ) ;
			resultExp.add("" + result2 + " ÷ " + exp.get(6).replaceAll("\\s","") + " = " + result3);
		}

		if (Data.GAME_MODE != Data.GAME_MODE_EASY){
			//handle 4th operator
			int result4 = 0;
			oper = exp.get(7).replaceAll("\\s","");

			if ( oper.equalsIgnoreCase("-") ){
				result4 = result3 - Integer.parseInt(exp.get(8).replaceAll("\\s",""));
				resultExp.add("" + result3 + " - " + exp.get(8).replaceAll("\\s","") + " = " + result4);
			}

			else if ( oper.equalsIgnoreCase("+") ){
				result4 = result3 + Integer.parseInt(exp.get(8).replaceAll("\\s",""));
				resultExp.add("" + result3 + " + " + exp.get(8).replaceAll("\\s","") + " = " + result4);
			}

			else if ( oper.equalsIgnoreCase("x") ){
				result4 = result3 *  Integer.parseInt(exp.get(8).replaceAll("\\s",""));
				resultExp.add("" + result3 + " x " + exp.get(8).replaceAll("\\s","") + " = " + result4);
			}

			else if ( oper.equalsIgnoreCase("÷") ){
				result4 = (int) ( result3 / Integer.parseInt(exp.get(8).replaceAll("\\s","") )) ;
				resultExp.add("" + result3 + " ÷ " + exp.get(8).replaceAll("\\s","") + " = " + result4);
			}
		}

		String returnString = "";
		for(int i = 0 ; i < resultExp.size(); i ++){

			returnString += resultExp.get(i);

			if (i != resultExp.size() - 1)
				returnString += "\n";
		}

		System.out.println("ankur RESULT = " +returnString);
		return returnString;
	}

	private final ClickListener clickListener = new ClickListener(this);

	private static class ClickListener implements OnClickListener{

		private final WeakReference<GameOverScreen> actInstance ;

		public ClickListener(GameOverScreen activity){
			actInstance = new WeakReference<GameOverScreen>(activity);
		}

		@Override
		public void onClick(View v) {

			GameOverScreen gameScreen = actInstance.get();
			Sound.playSound(Sound.SOUND_GENERIC_PRESS);

			if (v.getId() == gameScreen.tvRestart.getId()){

				Intent intent = new Intent(gameScreen, ModeSelectionScreen.class);
				intent.putExtra("show_ad", true);
				gameScreen.startActivity(intent);
				gameScreen.overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				gameScreen.finish();
			}
			else if (v.getId() == gameScreen.tvLeader.getId()){

				if (gameScreen.mConnected)
				{
					gameScreen.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameScreen.googleApiClient, gameScreen.getResources().getString(R.string.leaderboard_einsteins)),11);
					return; 
				}
				gameScreen.beginUserInitiatedSignIn();
			}

			else if (v.getId() == gameScreen.tvAchieve.getId()){

				if (gameScreen.mConnected)
				{
					gameScreen.startActivityForResult(Games.Achievements.getAchievementsIntent(gameScreen.googleApiClient), 10 );
					return; 
				}
				gameScreen.beginUserInitiatedSignIn();
			}
			else if (v.getId() == gameScreen.tvShare.getId()){
				gameScreen.shareit();
			}
		}
	}

	private void shareit(){ 
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		emailIntent.putExtra("android.intent.extra.TEXT", "Hey word lover, try this out: "+Utils.NUMBERS_GO_ROUND_URL_HTTP);
		emailIntent.setType("text/plain");
		startActivity(Intent.createChooser(emailIntent, "Share!"));
	}


}
