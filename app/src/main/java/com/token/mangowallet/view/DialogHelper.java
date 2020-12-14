package com.token.mangowallet.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.qmuiteam.qmui.layout.QMUIFrameLayout;
import com.qmuiteam.qmui.skin.QMUISkinHelper;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.skin.QMUISkinValueBuilder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.token.mangowallet.R;
import com.token.mangowallet.listener.DialogConfirmListener;
import com.token.mangowallet.utils.Constants;

import java.util.List;

import static com.token.mangowallet.utils.Constants.WalletType.BTC;
import static com.token.mangowallet.utils.Constants.WalletType.EOS;
import static com.token.mangowallet.utils.Constants.WalletType.ETH;
import static com.token.mangowallet.utils.Constants.WalletType.MGP;

public class DialogHelper {
    private static int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    public static void showRationaleDialog(Context context, PermissionUtils.OnRationaleListener.ShouldRequest shouldRequest) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setTitle(StringUtils.getString(android.R.string.dialog_alert_title))
                .setMessage(StringUtils.getString(R.string.permission_rationale_message))
                .setSkinManager(QMUISkinManager.defaultInstance(context))
                .addAction(StringUtils.getString(android.R.string.cancel), new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        shouldRequest.again(false);
                    }
                })
                .addAction(0, StringUtils.getString(android.R.string.ok), QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        shouldRequest.again(true);
                    }
                })
                .create().show();
    }

    public static QMUIDialog showEditTextDialog(Context context, String title, String hint, String ok, String cancel, DialogConfirmListener listener, boolean isPassword) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle(title)
                .setSkinManager(QMUISkinManager.defaultInstance(context))
                .setPlaceholder(hint)
                .setInputType(isPassword ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT)
                .addAction(cancel, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        builder.getEditText().setText("");
                        dialog.dismiss();
                    }
                })
                .addAction(ok, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        listener.onClick(dialog, builder.getEditText(), index);
                    }
                });

        return builder.create(mCurrentDialogStyle);
    }

    public static QMUIDialog showMessageDialog(Context context, String title, String message, String textbtn1, String textbtn2,
                                               QMUIDialogAction.ActionListener listener1, QMUIDialogAction.ActionListener listener2) {
        QMUIDialog.MessageDialogBuilder builder = new QMUIDialog.MessageDialogBuilder(context)
                .setSkinManager(QMUISkinManager.defaultInstance(context))
                .setMessage(message)
                .setCancelable(false);
        if (!ObjectUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!ObjectUtils.isEmpty(textbtn1)) {
            builder.addAction(textbtn1, listener1);
        }
        if (!ObjectUtils.isEmpty(textbtn2)) {
            builder.addAction(textbtn2, listener2);
        }
        QMUIDialog qmuiDialog = builder.create(mCurrentDialogStyle);
        return qmuiDialog;
    }

    public static QMUIBottomSheet showSimpleBottomSheetList(Activity activity, boolean gravityCenter, boolean addCancelBtn, boolean withIcon,
                                                            CharSequence title, boolean allowDragDismiss, boolean withMark, Object data,
                                                            QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener listener) {
        QMUIBottomSheet.BottomListSheetBuilder builder = new QMUIBottomSheet.BottomListSheetBuilder(activity);
        builder.setGravityCenter(gravityCenter)
                .setSkinManager(QMUISkinManager.defaultInstance(activity))
                .setTitle(title)
                .setAddCancelBtn(addCancelBtn)
                .setAllowDrag(allowDragDismiss)
                .setNeedRightMark(withMark)
                .setOnSheetItemClickListener(listener);
//                        new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
//                    @Override
//                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
//                        dialog.dismiss();
//
//                    }
//                });
//        if (withMark) {
//            builder.setCheckedIndex(40);
//        }
        if (data instanceof List) {
            for (int i = 0; i < ((List) data).size(); i++) {
                Object obj = ((List) data).get(i);
                if (obj instanceof Constants.WalletType) {
                    if ((Constants.WalletType) obj == MGP) {
                        builder.addItem(R.mipmap.mgp_icon, MGP + "", "Mango");
                    } else if ((Constants.WalletType) obj == ETH) {
                        builder.addItem(R.mipmap.ic_eth, ETH + "", "Ethereum");
                    } else if ((Constants.WalletType) obj == BTC) {
                        builder.addItem(R.mipmap.ic_btc, BTC + "", "Bitcoin");
                    } else if ((Constants.WalletType) obj == EOS) {
                        builder.addItem(R.mipmap.ic_eos, EOS + "", "Enterprise Operation System");
                    }
                }
            }
        }
//        for (int i = 1; i <= itemCount; i++) {
//            if (withIcon) {
//                builder.addItem()
//            } else {
//                builder.addItem("Item " + i);
//            }
//
//        }
        return builder.build();
    }

    public static QMUIFullScreenPopup showImage(Context context, AppCompatImageView imageView, int size) {

        QMUIFrameLayout frameLayout = new QMUIFrameLayout(context);
        frameLayout.setBackground(QMUIResHelper.getAttrDrawable(context, R.attr.qmui_skin_support_popup_bg));

        frameLayout.setRadius(QMUIDisplayHelper.dp2px(context, 5));
        int padding = QMUIDisplayHelper.dp2px(context, 20);
        frameLayout.setPadding(padding, padding, padding, padding);

        size = QMUIDisplayHelper.dp2px(context, size);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
        frameLayout.addView(imageView, lp);
        QMUIFullScreenPopup popup = QMUIPopups.fullScreenPopup(context)
                .addView(frameLayout)
                .closeBtn(true)
                .onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
                    @Override
                    public void onBlankClick(QMUIFullScreenPopup popup) {
                        //点击到空白区域
                    }
                })
                .onDismiss(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                    }
                });
        return popup;
    }

    public static QMUIRadiusImageView getImageView(Context context) {
        QMUIRadiusImageView imageView = new QMUIRadiusImageView(context);
        imageView.setBorderWidth(0);
        imageView.setCornerRadius(QMUIDisplayHelper.dp2px(context, 5));
        return imageView;
    }

    /**
     * 显示更新对话框
     *
     * @param remark 要更新的内容说明
     */
    public static Dialog showUpdateDialog(Context context, String remark, View.OnClickListener listener) {
        View view = View.inflate(context, R.layout.dialog_updata, null);
        final Dialog updateDialog = new Dialog(context, R.style.loading_dialog);
        updateDialog.setContentView(view);
        updateDialog.setCancelable(false);
        updateDialog.setCanceledOnTouchOutside(false);
        updateDialog.show();

        RelativeLayout detailsLayout = (RelativeLayout) view.findViewById(R.id.detailsLayout);
        TextView udpata_view_text = (TextView) view.findViewById(R.id.updateInstructionsTv);
        Button dialog_update_download_bt = (Button) view.findViewById(R.id.completeBtn);
        ImageView updateIv = (ImageView) view.findViewById(R.id.updateIv);
        ImageView cancelBtn = (ImageView) view.findViewById(R.id.cancelBtn);
        udpata_view_text.setText(remark);
        int height = SizeUtils.getMeasuredHeight(updateIv);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) detailsLayout.getLayoutParams();
        params.topMargin = height / 2;
        detailsLayout.setLayoutParams(params);
        detailsLayout.requestLayout();

        cancelBtn.setVisibility(View.GONE);
        dialog_update_download_bt.setOnClickListener(listener);
        cancelBtn.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                updateDialog.dismiss();
            }
        });
//        new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                updateDialog.dismiss();
//                downloadApk(url);
//            }
//        });
        return updateDialog;
    }

    public static QMUIDialog showEdit2Dialog(Context context, String title, String textbtn1, String textbtn2,
                                             QMUIDialogAction.ActionListener listener1, QMUIDialogAction.ActionListener listener2) {
        QMUIDialog.CustomDialogBuilder dialogBuilder = new QMUIDialog.CustomDialogBuilder(context);
        dialogBuilder.setLayout(R.layout.dialog_edit2);
        return dialogBuilder
                .setTitle(title)
                .addAction(textbtn1, listener1)
                .addAction(textbtn2, listener2)
                .create(mCurrentDialogStyle);
    }
}
