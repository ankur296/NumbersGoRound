package corp.seedling.numbersgoround.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import corp.seedling.numbersgoround.R;

public class AchievementsUnlocked {

	private Activity mActivity;
	public GoogleApiClient mGoogleApiClient;
	private SharedPreferences preferences;

	public AchievementsUnlocked(Activity app, GoogleApiClient googleApiClient) {
		mActivity = app;
		mGoogleApiClient = googleApiClient;
		preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
	}

	public void case10() {

		if (preferences.getBoolean("Achievement_10", true) && mGoogleApiClient != null) {
			preferences.edit().putBoolean("Achievement_10", false).apply();
			UnlockAchievements(mActivity.getString(R.string.achievement_pawn_of_maths));
		}

	}

	public void case50() {

		if (preferences.getBoolean("Achievement_50", true) && mGoogleApiClient != null) {
			preferences.edit().putBoolean("Achievement_50", false).apply();
			UnlockAchievements(mActivity.getString(R.string.achievement_knight_of_maths));
		}

	}

	public void case100() {

		if (preferences.getBoolean("Achievement_100", true) && mGoogleApiClient != null) {
			preferences.edit().putBoolean("Achievement_100", false).apply();
			UnlockAchievements(mActivity.getString(R.string.achievement_bishop_of_maths));
		}
	}

	public void case150() {
		
		if (preferences.getBoolean("Achievement_150", true) && mGoogleApiClient != null) {
			preferences.edit().putBoolean("Achievement_150", false).apply();
			UnlockAchievements(mActivity.getString(R.string.achievement_rook_of_maths));
		}
	}
	
	public void case200() {
		
		if (preferences.getBoolean("Achievement_200", true) && mGoogleApiClient != null) {
			preferences.edit().putBoolean("Achievement_200", false).apply();
			UnlockAchievements(mActivity.getString(R.string.achievement_king_of_maths));
		}
	}
	

	public void UnlockAchievements(String id) {
		if (mGoogleApiClient != null)
			Games.Achievements.unlock(mGoogleApiClient, id);

	}

}
