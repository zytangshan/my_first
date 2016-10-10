package com.example.spreadtrumdavidzhao.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by SPREADTRUM\david.zhao on 16-9-1.
 */
public class TestFragment extends Fragment {
    private Test mTest;
    private EditText mEdit;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private DateFormat mFormat;
    private ImageButton imageButton;
    private ImageView imageView;
    private Button mSendbutton;
    private Button mChoicebutton;
    private static final String IMAGE = "image";

    public static final String EXTRA_TEST_ID = "test_id";
    public static final String DIALOG_DATE = "Date";
    public static final int REQUEST_CODE = 0;
    public static final int REQUEST_PHOTO = 1;
    public static final int REQUEST_CONTACT = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTest = new Test();

        //UUID testId = (UUID)getActivity().getIntent()
        //        .getSerializableExtra(EXTRA_TEST_ID);
        UUID testId = (UUID) getArguments().getSerializable(EXTRA_TEST_ID);
        mTest = TestLab.getInstance(getActivity()).getTest(testId);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.testfragment,container,false);
        mEdit = (EditText)v.findViewById(R.id.mttitle);
        if (mTest != null) {
            mEdit.setText(mTest.getTitle());
        }

        mDateButton = (Button)v.findViewById(R.id.mbuttondate);
        mSolvedCheckbox = (CheckBox)v.findViewById(R.id.mSolved);
        imageButton = (ImageButton)v.findViewById(R.id.test_camera_button);
        imageView = (ImageView)v.findViewById(R.id.test_picture);
        mSendbutton = (Button)v.findViewById(R.id.test_send_button);
        mChoicebutton = (Button)v.findViewById(R.id.test_add_button);

        mChoicebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                PackageManager pm = getActivity().getPackageManager();
                List<ResolveInfo>activity = pm.queryIntentActivities(intent,0);
                if (activity.size() > 0) {
                    startActivityForResult(intent,REQUEST_CONTACT);

                }
            }
        });
        if (mTest.getmPerson()!= null) {
            mChoicebutton.setText(mTest.getmPerson());
        }

        mSendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getTestReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, mTest.getmPerson());
                intent = Intent.createChooser(intent, "send report");
                PackageManager pm = getActivity().getPackageManager();
                List<ResolveInfo> activity = pm.queryIntentActivities(intent, 0);
                if (activity.size() > 0) {
                    startActivity(intent);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mTest.getmPhoto();
                if (p == null) {
                    return;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();

                ImageFragment.newInstance(path).show(fm,IMAGE);
            }
        });

        if (mTest != null) {
            mSolvedCheckbox.setChecked(mTest.ismSolved());
        }

        if (mEdit != null) {
            mEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //mTest.setTitle("hello");

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });
        }
        if (mDateButton != null) {
            if (mTest != null) {
                String timeText = mFormat.format("MM/dd/yy h:mmaa",mTest.getmDate()).toString();
                mDateButton.setText(timeText);
            }



            //mDateButton.setEnabled(false);
            mDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    //DatePickFragment datePickFragment = new DatePickFragment();
                    DatePickFragment datePickFragment = DatePickFragment
                            .newInstance(mTest.getmDate());
                    datePickFragment.setTargetFragment(TestFragment.this,REQUEST_CODE);
                    datePickFragment.show(fm,DIALOG_DATE);
                }
            });
        }
        if(mSolvedCheckbox != null) {
            mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mTest.setmSolved(isChecked);
                }
            });
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TestCameraActivity.class);
                startActivityForResult(intent,REQUEST_PHOTO);
                //startActivity(intent);
            }
        });

        //if camera not available
        PackageManager pm = getActivity().getPackageManager();
        Boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
                Camera.getNumberOfCameras() > 0 ;
        if (!hasCamera) {
            imageButton.setEnabled(false);
        }
        return v ;
    }

    public static TestFragment newInstance(UUID testId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TEST_ID,testId);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE) {
            Date date = (Date)data.getSerializableExtra(DatePickFragment.EXTRA_DATE);
            mTest.setmDate(date);
            mDateButton.setText(mTest.getmDate().toString());
        }

        if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(TestCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                //Log.e("david"," filename :" + filename);
                Photo photo = new Photo(filename);
                mTest.setmPhoto(photo);

                if (mTest.getmPhoto() == null) {
                    Log.e("david","set fail");
                }else {
                    Log.e("david","set success");
                }

                showPhoto();
                Log.e("david",mTest.getTitle() + "has photo");
            }
        }

        if (requestCode == REQUEST_CONTACT) {
            Uri contacturi = data.getData();
            String[] queryfields = new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            Cursor cursor = getActivity().getContentResolver()
                    .query(contacturi,queryfields,null,null,null);

            if (cursor.getCount() == 0) {
                cursor.close();
                return;
            }
            cursor.moveToFirst();
            String person = cursor.getString(0);
            mTest.setmPerson(person);
            mChoicebutton.setText(person);
            cursor.close();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_test_menu,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        TestLab.getInstance(getActivity()).saveTests();
    }

    private void showPhoto() {
        Photo p = mTest.getmPhoto();
        BitmapDrawable bitmapDrawable = null;
        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            bitmapDrawable = PictureUtil.getScaledDrawable(getActivity(),path);
        }
        imageView.setImageDrawable(bitmapDrawable);
    }

    @Override
    public void onStart() {
        super.onStart();
         showPhoto();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtil.cleanImageView(imageView);
    }

    public String getTestReport() {
        String solved = null;
        if (mTest.ismSolved()) {
            solved = "good";
        }else {
            solved = "bad";
        }

        String dateformat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateformat,mTest.getmDate()).toString();

        String person = mTest.getmPerson();
        if (person == null) {
            person = "no person";
        }

        String report = mTest.getTitle() + dateString + solved + person;
        return report;

    }
}
