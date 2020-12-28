package org.operatorfoundation.shadowexampleapplicationjava;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.operatorfoundation.shapeshifter.shadow.java.ShadowConfig;
import org.operatorfoundation.shapeshifter.shadow.java.ShadowSocket;
import org.operatorfoundation.shapeshifter.shadow.java.ShadowSocketFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

//import okhttp3.OkHttpClient;
//import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    String httpRequest = "GET / HTTP/1.0\r\n\r\n";
    byte[] textBytes = httpRequest.getBytes();
    byte[] buffer = new byte[1024];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View chachaButton = findViewById(R.id.chaChaTest);
        chachaButton.setOnClickListener(this::onClickChaCha);

        View aesButton = findViewById(R.id.aesTest);
        aesButton.setOnClickListener(this::onClickAES);

        View okhttpAesButton = findViewById(R.id.okhttpAes);
        okhttpAesButton.setOnClickListener(this::onClickOkHTTPAES);

    }

    public void onClickOkHTTPAES(View v) {

        TextView outcome = findViewById(R.id.outcome);
        TextView output = findViewById(R.id.output);

        outcome.setText("");
        output.setText("");


        new Thread(() -> {
            // Create the socket
            String host = "159.203.158.90";
            int port = 2346;

            ShadowConfig sConfig = new ShadowConfig("1234", "AES-128-GCM");
            OkHttpClient client = new OkHttpClient.Builder()
                    .socketFactory(new ShadowSocketFactory(sConfig, host, port)).build();


            Request request = new Request.Builder().url("http://foo.com").build();

            try {
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                runOnUiThread(() -> {
                    try {
                        if (body != null) {
                            output.setText(body.string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        output.setText(String.valueOf(e));
                        outcome.setText(R.string.Fail);

                    }
                    if (response.code() == 200) {
                        outcome.setText(R.string.Success);
                    } else {
                        outcome.setText(R.string.Fail);
                    }
                });
            } catch (IOException e) {
                runOnUiThread(() -> {
                    e.printStackTrace();
                    output.setText(String.valueOf(e));
                    outcome.setText(R.string.Fail);
                });
            }

        }).start();
    }

    public void onClickOkHTTPChaCha(View v) {
        String host = "myshadowhost.org";
        int port = 8989;

        ShadowConfig sConfig = new ShadowConfig("secret", "password");
        OkHttpClient client = new OkHttpClient.Builder()
                .socketFactory(new ShadowSocketFactory(sConfig, host, port)).build();

        Request request = new Request.Builder().url("https://foo.com").build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickChaCha(View v) {

        TextView outcome = findViewById(R.id.outcome);
        TextView output = findViewById(R.id.output);

        outcome.setText("");
        output.setText("");

        new Thread(() -> {
            try {
                // Create the socket
                ShadowConfig config = new ShadowConfig("1234", "CHACHA20-IETF-POLY1305");
                ShadowSocket shadowSocket = new ShadowSocket(config, "159.203.158.90", 2345);

                // Send a request to the server
                shadowSocket.getOutputStream().write(textBytes);
                shadowSocket.getOutputStream().flush();

                // Read the data
                int bytesRead = shadowSocket.getInputStream().read(buffer);
                byte[] result = new byte[bytesRead];
                System.arraycopy(buffer, 0, result, 0, bytesRead);
                System.out.println(new String(result));

                // Closes the socket
                shadowSocket.close();

                runOnUiThread(() -> {
                    if (bytesRead == 0) {
                        outcome.setText(R.string.Fail);
                        output.setText(R.string.Empty_String);
                    } else {
                        outcome.setText(R.string.Success);
                        output.setText(new String(result));
                    }
                });


            } catch (IOException | NoSuchAlgorithmException e) {

                runOnUiThread(() -> {
                    outcome.setText(R.string.Fail);
                    output.setText(e.toString());
                });
                e.printStackTrace();
            }
        }).start();
    }

    public void onClickAES(View v) {

        TextView outcome = findViewById(R.id.outcome);
        TextView output = findViewById(R.id.output);

        outcome.setText("");
        output.setText("");


        new Thread(() -> {
            try {
                // Create the socket
                ShadowConfig config = new ShadowConfig("1234", "AES-128-GCM");
                ShadowSocket shadowSocket = new ShadowSocket(config, "159.203.158.90", 2346);

                // Send a request to the server
                shadowSocket.getOutputStream().write(textBytes);
                shadowSocket.getOutputStream().flush();

                // Read the data
                int bytesRead = shadowSocket.getInputStream().read(buffer);
                byte[] result = new byte[bytesRead];
                System.arraycopy(buffer, 0, result, 0, bytesRead);
                System.out.println(new String(result));

                // Closes the socket
                shadowSocket.close();

                runOnUiThread(() -> {
                    if (bytesRead == 0) {
                        outcome.setText(R.string.Fail);
                        output.setText(R.string.Empty_String);
                    } else {
                        outcome.setText(R.string.Success);
                        output.setText(new String(result));
                    }
                });


            } catch (IOException | NoSuchAlgorithmException e) {

                runOnUiThread(() -> {
                    outcome.setText(R.string.Fail);
                    output.setText(e.toString());
                });
                e.printStackTrace();
            }
        }).start();
    }

//    public static class MyNetwork extends Activity {
//        private static final String TAG = "ShadowConnect";
//        static ShadowConfig config;
//        static ShadowSocket socket;
//    }
//
//    public void ShadowConnect(Context applicationContext) throws IOException {
//        try {
//
//            ShadowConfig config = new ShadowConfig("1234", "AES-128-GCM");
//            ShadowSocket socket = new ShadowSocket(config, "1234", 2222);
//            //**********************Operaotor************
//            String plaintext = "GET / HTTP/1.0\r\n\r\n";
//            byte[] textBytes = plaintext.getBytes();
//            try {
//                socket.getOutputStream().write(textBytes);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                socket.getOutputStream().flush();
//                byte[] textOutput = new byte[2];
//                System.out.println("Output.before read" + textOutput.toString());
//                socket.getInputStream().read(textOutput);
//                System.out.println("Output after read" + textOutput.toString());
//
//                socket.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//    }
}

