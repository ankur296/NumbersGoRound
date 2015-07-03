package corp.seedling.numbersgoround.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import corp.seedling.numbersgoround.R;

public class Utils {

	public static final int SDK_VERSION = Build.VERSION.SDK_INT;
	
	public static final String NUMBERS_GO_ROUND_URL_HTTP = "https://play.google.com/store/apps/details?id=corp.seedling.seven.letters";
	public static final String SEVEN_LETTERS_URL = "market://details?id=corp.seedling.seven.letters";
	public static final String PUBLISHER_URL = "market://search?q=pub:Seedling Corp";

	public static ArrayList<Integer> colorList;
	public static ArrayList<Integer> menuColorList;
	
	
	public static void prepColorList(){
		colorList = new ArrayList<Integer>();
		menuColorList = new ArrayList<Integer>();
		
		menuColorList.add(R.drawable.circle_pink);
		menuColorList.add(R.drawable.circle_blue);
		menuColorList.add(R.drawable.circle_blue_purple);
//		menuColorList.add(R.drawable.circle_blue_royal);
//		menuColorList.add(R.drawable.circle_red);
		menuColorList.add(R.drawable.circle_green_pak);
		menuColorList.add(R.drawable.circle_blue_dark);
//		menuColorList.add(R.drawable.circle_purple);
		menuColorList.add(R.drawable.circle_blue_cyan);
		menuColorList.add(R.drawable.circle_pink_light);
//		menuColorList.add(R.drawable.circle_green_mehndi);
		menuColorList.add(R.drawable.circle_green_dark);
//		menuColorList.add(R.drawable.circle_orange);
//		menuColorList.add(R.drawable.circle_green_shine);
		
		
		colorList.add(R.drawable.circle_blue);
		colorList.add(R.drawable.circle_blue_cyan);
		colorList.add(R.drawable.circle_blue_dark);
		colorList.add(R.drawable.circle_blue_purple);
		colorList.add(R.drawable.circle_blue_royal);
		colorList.add(R.drawable.circle_red);
		colorList.add(R.drawable.circle_pink);
		colorList.add(R.drawable.circle_pink_light);
//		colorList.add(R.drawable.circle_orange);
		colorList.add(R.drawable.circle_purple);
		colorList.add(R.drawable.circle_green_dark);
		colorList.add(R.drawable.circle_green_mehndi);
		colorList.add(R.drawable.circle_green_pak);
		colorList.add(R.drawable.circle_green_shine);
		
		Collections.shuffle(colorList);
		
	}

	public static int[] convertIntegers(List<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
	
	public static Typeface getFont(Context paramContext)
	{
		return Typeface.createFromAsset(paramContext.getResources().getAssets(), "ArchivoBlack.otf");
	}

}
