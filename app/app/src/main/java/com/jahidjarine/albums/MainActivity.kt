package com.jahidjarine.albums

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.widget.*
import android.graphics.Color

class MainActivity : Activity() {

    private val permissionCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                permissionCode
            )
        } else {
            showGallery()
        }
    }

    private fun showGallery() {

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(Color.WHITE)

        val title = TextView(this)
        title.text = "Jahid Jarine Albums"
        title.textSize = 24f
        title.setTextColor(Color.BLACK)
        title.gravity = Gravity.CENTER_VERTICAL
        title.setPadding(30, 30, 20, 30)

        root.addView(
            title,
            LinearLayout.LayoutParams(
                -1,
                80
            )
        )

        val grid = GridLayout(this)
        grid.columnCount = 3

        val projection = arrayOf(
            MediaStore.Images.Media._ID
        )

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(
                MediaStore.Images.Media._ID
            )

            while (it.moveToNext()) {

                val id = it.getLong(idColumn)

                val image = ImageView(this)

                val uri = android.content.ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                image.setImageURI(uri)
                image.scaleType = ImageView.ScaleType.CENTER_CROP

                val params = GridLayout.LayoutParams()
                params.width = 0
                params.height = 300
                params.columnSpec = GridLayout.spec(
                    GridLayout.UNDEFINED,
                    1f
                )
                params.setMargins(4, 4, 4, 4)

                grid.addView(image, params)
            }
        }

        val scroll = ScrollView(this)
        scroll.addView(grid)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                -1,
                0,
                1f
            )
        )

        setContentView(root)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == permissionCode &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            showGallery()
        } else {
            Toast.makeText(
                this,
                "Photos permission is required",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
