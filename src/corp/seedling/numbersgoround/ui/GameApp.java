package corp.seedling.numbersgoround.ui;

import corp.seedling.numbersgoround.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import corp.seedling.numbersgoround.utils.Sound;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

public class GameApp extends Application {

	private static GameApp appInstance;
	private MediaPlayer mp;
	SharedPreferences preferences;

	public static GameApp getAppInstance(){
		return appInstance;
	}

	@Override
	public void onCreate() {
		System.out.println("ankur game app oncreate");
		super.onCreate();
		appInstance = this; 

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Sound.initSound();

		if (preferences.getBoolean("music", true) == true){
			mp = MediaPlayer.create(this, R.raw.bg_music);
			mp.setLooping(true);
			mp.start();
		}
		
		initAdMobXML();
	}

	InterstitialAd interstitialAds;
	AdRequest.Builder adRequestBuilder ;

	public void initAdMobXML() {
		System.out.println("ankur start ad loading");
		interstitialAds = new InterstitialAd(this.getApplicationContext());
		interstitialAds.setAdUnitId("ca-app-pub-6816050713890894/7203452365");

		// Create an ad request.
		adRequestBuilder = new AdRequest.Builder();
//		adRequestBuilder.addTestDevice("BE0B283D9DB35079AECC55005262BED2");//TODO: Remove

		interstitialAds.loadAd(adRequestBuilder.build());
	}
	
	void loadAd(){
		if (!interstitialAds.isLoaded())
			interstitialAds.loadAd(adRequestBuilder.build());
	}
	void resumePlaying() {
		if (preferences.getBoolean("music", true) == true){

			if  (mp != null){

				if (!mp.isPlaying() ){

					mp.seekTo(preferences.getInt("seek_value", 0));
					mp.start();
				}
			}
			else{
				mp = MediaPlayer.create(this, R.raw.bg_music);
				mp.setLooping(true);
				mp.seekTo(preferences.getInt("seek_value", 0));
				mp.start();
			}
		}
	}

	void stopPlaying() {
		if (preferences.getBoolean("music", true) == true){

			if (mp != null) {

				mp.stop();
				preferences.edit().putInt("seek_value", mp.getCurrentPosition()).apply();
				mp.release();
				mp = null;
			}
		}
	}
}
