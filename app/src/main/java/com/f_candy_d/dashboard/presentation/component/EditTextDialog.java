package com.f_candy_d.dashboard.presentation.component;

import android.app.AlertDialog;
import android.app.Dialog;
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

    public interface TextChangeListener {
        void onTextChange(String text);
    }

    public interface ButtonClickListener {
        void onPositiveButtonClick(int tag, String text);
        void onNegativeButtonClick(int tag, String text);
    }

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_MESSAGE = "arg_message";
    private static final String ARG_NEGATIVE_BUTTON_TITLE = "arg_negative_button_title";
    private static final String ARG_POSITIVE_BUTTON_TITLE = "arg_positive_button_title";
    private static final String ARG_TAG = "arg_tag";
    private static final String ARG_EDIT_TEXT_HINT = "arg_edit_text_hint";
    private static final String ARG_EDIT_TEXT = "arg_edit_text";

    private String mTitle;
    private String mMessage;
    private String mNegativeButtonTitle;
    private String mPositiveButtonTitle;
    private int mTag;

    private TextChangeListener mTextChangeListener;
    private ButtonClickListener mButtonClickListener;

    public static EditTextDialog newInstance
            (String title, String message, String positiveButtonTitle, String negativeButtonTitle, String editTextHint, String editText,int tag) {

        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_MESSAGE, message);
        bundle.putString(ARG_NEGATIVE_BUTTON_TITLE, negativeButtonTitle);
        bundle.putString(ARG_POSITIVE_BUTTON_TITLE, positiveButtonTitle);
        bundle.putString(ARG_EDIT_TEXT_HINT, editTextHint);
        bundle.putString(ARG_EDIT_TEXT, editText);
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
                if (mTextChangeListener != null) {
                    mTextChangeListener.onTextChange(editable.toString());
                }
            }
        });

        if (getArguments() != null) {
            inputEditText.setText(getArguments().getString(ARG_EDIT_TEXT, null));
            TextInputLayout inputLayout = content.findViewById(R.id.text_input_layout);
            inputLayout.setHint(getArguments().getString(ARG_EDIT_TEXT_HINT, null));

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
                    if (mButtonClickListener != null) {
                        mButtonClickListener.onPositiveButtonClick(mTag, inputEditText.getText().toString());
                    }
                }
            });
        }

        if (mNegativeButtonTitle != null) {
            builder.setNegativeButton(mNegativeButtonTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mButtonClickListener != null) {
                        mButtonClickListener.onNegativeButtonClick(mTag, inputEditText.getText().toString());
                    }
                }
            });
        }

        return builder.create();
    }

    public void setTextChangeListener(TextChangeListener textChangeListener) {
        mTextChangeListener = textChangeListener;
    }

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        mButtonClickListener = buttonClickListener;
    }

    /**
     * BaseEditor
     * --------------------------------------------------------------------------- */

    public static class Builder {

        private String mTitle;
        private String mMessage;
        private String mNegativeButtonTitle;
        private String mPositiveButtonTitle;
        private String mEditTextHint;
        private String mEditText;
        private int mTag;
        private TextChangeListener mTextChangeListener;
        private ButtonClickListener mButtonClickListener;

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

        public Builder editText(String text) {
            mEditText = text;
            return this;
        }

        public Builder tag(int tag) {
            mTag = tag;
            return this;
        }

        public Builder textChangeListener(TextChangeListener listener) {
            mTextChangeListener = listener;
            return this;
        }

        public Builder buttonClickListener(ButtonClickListener listener) {
            mButtonClickListener = listener;
            return this;
        }

        public EditTextDialog create() {
            EditTextDialog dialog = EditTextDialog.newInstance(
                    mTitle,
                    mMessage,
                    mPositiveButtonTitle,
                    mNegativeButtonTitle,
                    mEditTextHint,
                    mEditText,
                    mTag);

            dialog.setTextChangeListener(mTextChangeListener);
            dialog.setButtonClickListener(mButtonClickListener);

            return dialog;
        }
    }
}