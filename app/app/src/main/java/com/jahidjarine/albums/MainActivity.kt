package com.jahidjarine.albums

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.provider.MediaStore
import android.content.pm.PackageManager
import android.widget.GridView
import android.widget.ArrayAdapter

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    100
                )
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100
                )
            }
        }

        val gridView = GridView(this)
        gridView.numColumns = 3
        gridView.verticalSpacing = 8
        gridView.horizontalSpacing = 8

        val images = ArrayList<String>()

        val projection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME
        )

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val nameIndex =
                cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                if (nameIndex >= 0) {
                    images.add(cursor.getString(nameIndex))
                }
            }
        }

        gridView.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            images
        )

        setContentView(gridView)
    }
}
