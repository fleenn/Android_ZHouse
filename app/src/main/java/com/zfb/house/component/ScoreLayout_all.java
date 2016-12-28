package com.zfb.house.component;
/**
 * 整颗星评分控件
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zfb.house.R;


public class ScoreLayout_all extends LinearLayout implements OnClickListener{
	private ImageView scoreOne,scoreTwo,scoreThree,scoreFour,scoreFive;
	private TextView tvTitle;
	boolean isOneSelect,isTwoSelect,isThreeSelect,isFourSelect,isFiveSelect;
	private int scoreNumber = 5;

	public int getScoreNumber() {
		return scoreNumber;
	}

	public ScoreLayout_all(Context context) {
		super(context);
	}
	
	public ScoreLayout_all(Context context, AttributeSet attrs) {
			super(context, attrs);
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inflater.inflate(R.layout.linearlayout_score,this);
		    tvTitle = (TextView) findViewById(R.id.score_number);
			scoreOne = (ImageView) findViewById(R.id.score_one);
			scoreTwo = (ImageView) findViewById(R.id.score_two);
			scoreThree = (ImageView) findViewById(R.id.score_three);
			scoreFour = (ImageView) findViewById(R.id.score_four);
			scoreFive = (ImageView) findViewById(R.id.score_five);
			scoreOne.setOnClickListener(this);
			scoreTwo.setOnClickListener(this);
			scoreThree.setOnClickListener(this);
			scoreFour.setOnClickListener(this);
			scoreFive.setOnClickListener(this);

			scoreNumber = 5;
			isOneSelect = true;
			isTwoSelect = true;
			isThreeSelect = true;
			isFourSelect = true;
			isFiveSelect = true;
			scoreOne.setBackgroundResource(R.drawable.cos_star_selected);
			scoreTwo.setBackgroundResource(R.drawable.cos_star_selected);
			scoreThree.setBackgroundResource(R.drawable.cos_star_selected);
			scoreFour.setBackgroundResource(R.drawable.cos_star_selected);
			scoreFive.setBackgroundResource(R.drawable.cos_star_selected);
	}

	public void setTitle(String title){
		tvTitle.setText(title);
	}

	private void setImageBg(int index){
		switch (index) {
		case 1:
			if (isOneSelect) {
				scoreNumber = 0;
				isOneSelect = false;
				isTwoSelect = false;
				isThreeSelect = false;
				isFourSelect = false;
				isFiveSelect = false;
				scoreOne.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreTwo.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreThree.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreFour.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreFive.setBackgroundResource(R.drawable.cos_star_unselected);
			}else{
				scoreNumber = 1;
				isOneSelect = true; 
				scoreOne.setBackgroundResource(R.drawable.cos_star_selected);
			}
			break;
		case 2:
			if (isTwoSelect) {
				scoreNumber = 1;
				isTwoSelect = false;
				isThreeSelect = false;
				isFourSelect = false;
				isFiveSelect = false;
				scoreTwo.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreThree.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreFour.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreFive.setBackgroundResource(R.drawable.cos_star_unselected);
			}else{
				scoreNumber = 2;
				isOneSelect = true; 
				isTwoSelect = true;
				scoreOne.setBackgroundResource(R.drawable.cos_star_selected);
				scoreTwo.setBackgroundResource(R.drawable.cos_star_selected);
			}
			break;
		case 3:
			if (isThreeSelect) {
				scoreNumber = 2;
				isThreeSelect = false;
				isFourSelect = false;
				isFiveSelect = false;
				scoreThree.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreFour.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreFive.setBackgroundResource(R.drawable.cos_star_unselected);
			}else{
				scoreNumber = 3;
				isOneSelect = true; 
				isTwoSelect = true;
				isThreeSelect = true;
				scoreOne.setBackgroundResource(R.drawable.cos_star_selected);
				scoreTwo.setBackgroundResource(R.drawable.cos_star_selected);
				scoreThree.setBackgroundResource(R.drawable.cos_star_selected);
			}
			break;
		case 4:
			if (isFourSelect) {
				scoreNumber = 3;
				isFourSelect = false;
				isFiveSelect = false;
				scoreFour.setBackgroundResource(R.drawable.cos_star_unselected);
				scoreFive.setBackgroundResource(R.drawable.cos_star_unselected);
			}else{
				scoreNumber = 4;
				isOneSelect = true; 
				isTwoSelect = true;
				isThreeSelect = true;
				isFourSelect = true;
				scoreOne.setBackgroundResource(R.drawable.cos_star_selected);
				scoreTwo.setBackgroundResource(R.drawable.cos_star_selected);
				scoreThree.setBackgroundResource(R.drawable.cos_star_selected);
				scoreFour.setBackgroundResource(R.drawable.cos_star_selected);
			}
			break;
		case 5:
			if (isFiveSelect) {
				scoreNumber = 4;
				isFiveSelect = false;
				scoreFive.setBackgroundResource(R.drawable.cos_star_unselected);
			}else{
				scoreNumber = 5;
				isOneSelect = true; 
				isTwoSelect = true;
				isThreeSelect = true;
				isFourSelect = true;
				isFiveSelect = true;
				scoreOne.setBackgroundResource(R.drawable.cos_star_selected);
				scoreTwo.setBackgroundResource(R.drawable.cos_star_selected);
				scoreThree.setBackgroundResource(R.drawable.cos_star_selected);
				scoreFour.setBackgroundResource(R.drawable.cos_star_selected);
				scoreFive.setBackgroundResource(R.drawable.cos_star_selected);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.score_one:
			setImageBg(1);
			break;
		case R.id.score_two:
			setImageBg(2);
			
			break;
		case R.id.score_three:
			setImageBg(3);
	
			break;
		case R.id.score_four:
			setImageBg(4);
	
			break;
		case R.id.score_five:
			setImageBg(5);
			break;

		default:
			break;
		}
		
	}
	
	

}
