package com.galileo.cc6.helpmate.projectFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.activities.DrawingView;
import com.galileo.cc6.helpmate.activities.NoteActivity;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelfNoteFragment extends Fragment {
    private View mRootView;
    private EditText mSubjectEditText, mBodyEditText;
    // Title of note
    private EditText noteTitle;
    static DrawingView drawingV;

    // EditText panel
    private EditText editText;
    private static final String LOG_TAG = "SelfNoteFragment";
    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 1;

    public SelfNoteFragment() {
        // Required empty public constructor
    }

    public static SelfNoteFragment newInstance(DrawingView drawingView){
        drawingV = drawingView;
        SelfNoteFragment fragment = new SelfNoteFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_self_note, container, false);
        permissionPdf();
        return mRootView;
    }

    public void permissionPdf() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                createPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } else {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "Se necesita permiso para guardar el archivo PDF", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE_RESULT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_EXTERNAL_STORAGE_RESULT) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Permiso fue rechazado, PDF no fue creado",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        NoteActivity activity = (NoteActivity) getActivity();
        String title = activity.getMyData()[0];
        String body = activity.getMyData()[1];

        if (title.equals("") && body.equals("")) {
            title = " ";
        }

        // Get default spannable value
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), title);
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
            Log.i(LOG_TAG, "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder + " - " + timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(myFile);
        OutputStream outImg = new FileOutputStream(pdfFolder + "Image.png");

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.open();

        //Step 4 Add content

        //Define a bitmap with the same size as the view
        Bitmap bit = Bitmap.createBitmap(drawingV.getWidth(), drawingV.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(bit);
        //Get the view's background
        Drawable bg =drawingV.getBackground();
        if (bg!=null)
            //has background drawable, then draw it on the canvas
            bg.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        drawingV.draw(canvas);

        bit.compress(Bitmap.CompressFormat.PNG, 100, outImg);

        try {
            Image img = Image.getInstance(pdfFolder + "Image.png");
            img.scaleToFit(document.getPageSize());
            img.setAbsolutePosition(0, 0);
            document.add(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        float fntSize = 30f;
        float lineSpacing = 0f;

        document.add(new Paragraph(new Phrase(5f,title,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 30f))));
        document.add(new Paragraph(new Phrase(40f,"\n\n" + body,
                FontFactory.getFont(FontFactory.TIMES_ROMAN, 20f))));

        //Step 5: Close the document
        document.close();
        promptForNextAction(myFile);
    }

    private void viewPdf(File myFile){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void emailNote(File myFile)
    {
        NoteActivity activity = (NoteActivity) getActivity();
        String title = activity.getMyData()[0];
        String body = activity.getMyData()[1];

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT,title);
        email.putExtra(Intent.EXTRA_TEXT, body);
        Uri uri = Uri.parse(myFile.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        startActivity(email);
    }

    private void promptForNextAction(final File myFile)
    {
        final String[] options = { getString(R.string.label_email), getString(R.string.label_preview),
                getString(R.string.label_cancel) };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Note Saved, What Next?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals(getString(R.string.label_email))){
                    emailNote(myFile);
                }else if (options[which].equals(getString(R.string.label_preview))){
                    viewPdf(myFile);
                }else if (options[which].equals(getString(R.string.label_cancel))){
                    dialog.dismiss();
                }
            }
        });

        builder.show();

    }
}
