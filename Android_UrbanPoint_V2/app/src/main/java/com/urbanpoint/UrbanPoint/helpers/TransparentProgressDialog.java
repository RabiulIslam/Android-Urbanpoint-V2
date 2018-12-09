/**
  * @author     Anurag Sethi <anurags@smartdatainc.net>
  * @version 1.0.0
  * @since 2014-07-28
  */
package com.urbanpoint.UrbanPoint.helpers;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.urbanpoint.UrbanPoint.R;

/**
 *
 * The class handles the project dialog operations 
 */
 
public class TransparentProgressDialog extends Dialog {
	
	@SuppressWarnings("unused")
	private ImageView iv;
	
	/**
	 * Constructor
	 * @since 2014-07-28
	 * @param context The Context from which the dialog is called
	 * @param resourceIdOfImage the ID for the waiting image to be shown
	 * @return none
	 */	
	public TransparentProgressDialog(Context context, int resourceIdOfImage) {
		super(context, R.style.TransparentProgressDialog);
        	WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        	wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        	getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		setContentView(R.layout.progresslayout);
	}
	
	/**
	 * Start the dialog and display it on screen.
	 * @since 2014-07-28
	 * @return none
	 */
	@Override
	public void show() {
		super.show();
		
	}
	
	/**
	 * Dismiss the dialog
	 * @since 2014-07-28
	 * @return none
	 */
	@Override
	public void dismiss() {
		super.dismiss();
		
	}
}