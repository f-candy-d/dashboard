package com.f_candy_d.dashboard.presentation.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.f_candy_d.dashboard.R;

/**
 * Created by daichi on 9/29/17.
 */

public class EditTextDialog extends DialogFragment {

    public interface NoticeListener {
        void onPositiveButtonClick(int tag, String text);
        void onNegativeButtonClick(int tag, String text);
        void onTextChange(String text);
    }

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_MESSAGE = "arg_message";
    private static final String ARG_NEGATIVE_BUTTON_TITLE = "arg_negative_button_title";
    private static final String ARG_POSITIVE_BUTTON_TITLE = "arg_positive_button_title";
    private static final String ARG_TAG = "arg_tag";
    private static final String ARG_EDIT_TEXT_HINT = "arg_edit_text_hint";

    private NoticeListener mListener;
    private String mTitle;
    private String mMessage;
    private String mNegativeButtonTitle;
    private String mPositiveButtonTitle;
    private String mEditTextHint;
    private int mTag;

    public static EditTextDialog newInstance
            (String title, String message, String positiveButtonTitle, String negativeButtonTitle, String editTextHint, int tag) {

        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_MESSAGE, message);
        bundle.putString(ARG_NEGATIVE_BUTTON_TITLE, negativeButtonTitle);
        bundle.putString(ARG_POSITIVE_BUTTON_TITLE, positiveButtonTitle);
        bundle.putString(ARG_EDIT_TEXT_HINT, editTextHint);
        bundle.putInt(ARG_TAG, tag);

        EditTextDialog fragment = new EditTextDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE, null);
            mMessage = getArguments().getString(ARG_MESSAGE, null);
            mNegativeButtonTitle = getArguments().getString(ARG_NEGATIVE_BUTTON_TITLE, null);
            mPositiveButtonTitle = getArguments().getString(ARG_POSITIVE_BUTTON_TITLE, null);
            mEditTextHint = getArguments().getString(ARG_EDIT_TEXT_HINT, null);
            mTag = getArguments().getInt(ARG_TAG);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View content = getActivity().getLayoutInflater().inflate(R.layout.edit_text_dialog_content, null);
        builder.setView(content);
        final TextInputEditText inputEditText = content.findViewById(R.id.text_input);
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mListener.onTextChange(editable.toString());
            }
        });

        if (mEditTextHint != null) {
            TextInputLayout inputLayout = content.findViewById(R.id.text_input_layout);
            inputLayout.setHint(mEditTextHint);
        }

        if (mTitle != null) {
            builder.setTitle(mTitle);
        }

        if (mMessage != null) {
            builder.setMessage(mMessage);
        }

        if (mPositiveButtonTitle != null) {
            builder.setPositiveButton(mPositiveButtonTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.onPositiveButtonClick(mTag, inputEditText.getText().toString());
                }
            });
        }

        if (mNegativeButtonTitle != null) {
            builder.setNegativeButton(mNegativeButtonTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.onNegativeButtonClick(mTag, inputEditText.getText().toString());
                }
            });
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NoticeListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString()
                    + " must implement EditTextDialog.NoticeListener interface");
        }
    }

    /**
     * Builder
     * --------------------------------------------------------------------------- */

    public static class Builder {

        private String mTitle;
        private String mMessage;
        private String mNegativeButtonTitle;
        private String mPositiveButtonTitle;
        private String mEditTextHint;
        private int mTag;

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder negativeButton(String title) {
            mNegativeButtonTitle = title;
            return this;
        }

        public Builder positiveButton(String title) {
            mPositiveButtonTitle = title;
            return this;
        }
        
        public Builder editTextHint(String hint) {
            mEditTextHint = hint;
            return this;
        }

        public Builder tag(int tag) {
            mTag = tag;
            return this;
        }

        public EditTextDialog create() {
            return EditTextDialog.newInstance(
                    mTitle,
                    mMessage,
                    mPositiveButtonTitle,
                    mNegativeButtonTitle,
                    mEditTextHint,
                    mTag);
        }
    }
}