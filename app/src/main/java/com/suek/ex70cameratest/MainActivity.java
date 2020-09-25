package com.suek.ex70cameratest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv= findViewById(R.id.iv);
    }





    public void clickFAB(View view) {
        //카메라 앱 실행하는 인텐트 생성
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //결과를 받아오도록..
        startActivityForResult(intent, 20);
    }


    //결과를 가지고 돌아온 인텐트..
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 20:
                //카메라 앱에서 캡쳐한 결과를 가져왔는지?
                if(resultCode == RESULT_OK){    //resultCode != RESULT_CANCEL 같은말
                    //디바이스마다 프로그래밍으로 실행한 카메라앱은 (사용자가 카메라앱을 직접 누른것이 아니라)
                    //사진캡쳐후 저장방식이 다름.
                    //마시멜로우 이후 버전부터 대부분의 디바이스에서는 '인텐트로 실행한 카메라 앱으로 찍은 사진을 디바이스로 저장하지 않음'. (하드디스크에 사진이 없고 램에만있음)
                    //저장하지 않고, 사진의 Bitmap 객체를 만들어 전달해줌
                    //즉, 사진의 경로인 Uri 가 없을 수 있다는 것임.(반면에, 사용자가 직접 카메라앱을 켜서 사진을 찍으면 하드디스크에 저장이 되어 경로를 찾을수있다.- 파일로 저장이 된다는 의미)

                    //먼저, 인텐트 객체에게 Uri 를 가져왔는지 알아보기 위해서..
                    Uri uri= data.getData();

                    if(uri != null){    //사진이 파일로 저장되는 방식의 디바이스
                        Toast.makeText(this, "URI로 저장", Toast.LENGTH_SHORT).show();
                        Glide.with(this).load(uri).into(iv);
                    }else {             //Bitmap 으로 전달되는 방식의 디바이스(저장이 안되었다는 의미)
                        Toast.makeText(this, "저장없이 Bitmap 으로", Toast.LENGTH_SHORT).show();

                        //Bitmap 객체를 Intent 의 Extra 데이터로 전달해줌
                        Bundle bundle= data.getExtras();    //자료형이 뭔지 모르니까 꾸러미 bundle 로 줌
                        Bitmap bm= (Bitmap) bundle.get("data");  //"data"는 약속이 되어있는 정해저있는 이름
                        Glide.with(this).load(bm).into(iv);

                        //Bitmap 으로 왔다는 것은 캡쳐한 사진이 디바이스에 저장되어있지 않다는 것임
                        //만약 파일에 저장하고 싶다면 꽤 복잡한 코드작업이 필요.. 다음 예제로..
                        //Bitmap 으로 전달된 데이ㅓ는 해상도가 낮은 셈네일 이미지임..
                        //파일저장을 해야 full-size 이미지가 전달됨..
                    }
                }
                break;
        }
    }
}
