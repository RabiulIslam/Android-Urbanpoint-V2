package com.urbanpoint.UrbanPoint.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.urbanpoint.UrbanPoint.R;



public class CustomAlert {
    private boolean isCustomAlertDialogCancelable = true;

    public void showCustomAlertDialog(final Context context, final String title, String msgToShow, String positiveBtnText, String negativeBtnText, boolean isNegativeButtonRequired, final CustomAlertConfirmationInterface customDialogConfirmationListener) {

        // custom dialog
        if (context != null) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.custome_message_alert_box);

            dialog.setCancelable(isCustomAlertDialogCancelable);

            // set the custom dialog components - text
            TextView messageTitle = (TextView) dialog.findViewById(R.id.messageTitle);
            TextView messageText = (TextView) dialog.findViewById(R.id.dialogMessageText);
            Button cancelButton = (Button) dialog.findViewById(R.id.dialogButtonCancel);
            Button okButton = (Button) dialog.findViewById(R.id.dialogButtonOK);


            TextView dialogButtonSeparator = (TextView) dialog.findViewById(R.id.dialogButtonSeparator);
            dialogButtonSeparator.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);

            if (msgToShow != null) {
                messageText.setText(msgToShow);
            }
            if (positiveBtnText != null) {
                okButton.setText(positiveBtnText);
            }

            if (title != null) {
                messageTitle.setText(title);
            }


            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    if (customDialogConfirmationListener != null)
                        customDialogConfirmationListener.callConfirmationDialogPositive();

                }
            });

            if (isNegativeButtonRequired) {

                dialogButtonSeparator.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);

                if (negativeBtnText != null) {
                    cancelButton.setText(negativeBtnText);
                }

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (customDialogConfirmationListener != null)
                            customDialogConfirmationListener.callConfirmationDialogNegative();
                        if (title != null && title.equalsIgnoreCase("Redeem")) {
                            // MyApplication.getInstance().trackEvent("Redemption: pop up", "button click", "Redemption: pop up - not present at the outlet");
                        } else if (title != null && title.equalsIgnoreCase(context.getResources().getString(R.string.logout))) {
                            // MyApplication.getInstance().trackEvent(context.getResources().getString(R.string.logout), "Logout No Click", "Logout Cancel");
                        }

                    }
                });
            }
            dialog.show();
        }
    }

    public void showContextualAlertDialog(final Context context, final CustomAlertConfirmationInterface customDialogConfirmationListener) {
        if (context != null) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_contextual_permision);

            dialog.setCancelable(isCustomAlertDialogCancelable);


            // set the custom dialog components - text
            Button cancelButton = dialog.findViewById(R.id.contextual_btn_no);
            Button okButton = dialog.findViewById(R.id.contextual_btn_yes);


            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    if (customDialogConfirmationListener != null)
                        customDialogConfirmationListener.callConfirmationDialogPositive();
                }
            });

            cancelButton.setVisibility(View.VISIBLE);


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (customDialogConfirmationListener != null)
                        customDialogConfirmationListener.callConfirmationDialogNegative();

                }
            });

            dialog.show();
        }
    }

    public void showUberAlertDialog(final Context context, final CustomAlertConfirmationInterface customDialogConfirmationListener) {

        // custom dialog
        if (context != null) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_contextual_permision);

            dialog.setCancelable(isCustomAlertDialogCancelable);

            // set the custom dialog components - text
            TextView messageTitle = dialog.findViewById(R.id.contextual_txv_1);
            TextView messageText = dialog.findViewById(R.id.contextual_txv_2);
            Button cancelButton = dialog.findViewById(R.id.contextual_btn_no);
            Button okButton = dialog.findViewById(R.id.contextual_btn_yes);


            messageText.setText(R.string.contextual_uber_messages);
            okButton.setText(R.string.contextual_uber_btn);


            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    if (customDialogConfirmationListener != null)
                        customDialogConfirmationListener.callConfirmationDialogPositive();
                }
            });

            cancelButton.setVisibility(View.VISIBLE);


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (customDialogConfirmationListener != null)
                        customDialogConfirmationListener.callConfirmationDialogNegative();

                }
            });

            dialog.show();
        }
    }

    public void showAppUpdateAlertDialog(final Context context, boolean _isForcefullyUpdateActive, final CustomAlertConfirmationInterface customDialogConfirmationListener) {

        if (context != null) {
            // custom dialog
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_app_update);
            boolean isCancelAble = !_isForcefullyUpdateActive;
            Log.d("ISCANCELLABLE", "showAppUpdateAlertDialog: "+isCancelAble);

            // set the custom dialog components - text
            Button cancelButton = dialog.findViewById(R.id.app_update_btn_no);
            Button okButton = dialog.findViewById(R.id.app_update_btn_yes);


            // if button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    if (customDialogConfirmationListener != null)
                        customDialogConfirmationListener.callConfirmationDialogPositive();
                }
            });

            if (!_isForcefullyUpdateActive) {
                cancelButton.setVisibility(View.VISIBLE);
                dialog.setCancelable(isCancelAble);
            } else {
                cancelButton.setVisibility(View.GONE);
                dialog.setCancelable(isCancelAble);
            }


            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (customDialogConfirmationListener != null)
                        customDialogConfirmationListener.callConfirmationDialogNegative();

                }
            });

            dialog.show();
        }
    }

    public void showCustomiOSMessageBox(final Context context, String message, int duration) {

        if (context != null) {
            // custom dialog
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom_message_box);
            dialog.setCancelable(true);
            // set the custom dialog components - text
            TextView messageText = (TextView) dialog.findViewById(R.id.dialogMessageText);

            messageText.setText("" + message);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, duration);
            dialog.show();
        }
    }

    public void showToast(Context mContext, String message, int durationForMessageToAppear) {
        if (mContext != null) {
            if (durationForMessageToAppear == 1) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        }

    }
}
