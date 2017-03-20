/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.cnmar.R;
import com.example.administrator.cnmar.activity.FailInBoxActivity;
import com.example.administrator.cnmar.activity.HalfProductInOrderDetailActivity;
import com.example.administrator.cnmar.activity.HalfProductOutOrderDetailActivity;
import com.example.administrator.cnmar.activity.InBoxActivity;
import com.example.administrator.cnmar.activity.PrevProcessOutBoxActivity;
import com.example.administrator.cnmar.activity.MaterialInOrderDetailActivity;
import com.example.administrator.cnmar.activity.MaterialOutOrderDetailActivity;
import com.example.administrator.cnmar.activity.ProduceManageActivity;
import com.example.administrator.cnmar.activity.ProductInOrderDetailActivity;
import com.example.administrator.cnmar.activity.ProductOutOrderDetailActivity;
import com.example.administrator.cnmar.activity.QualityControlActivity;
import com.example.administrator.cnmar.helper.SPHelper;
import com.example.administrator.cnmar.helper.UrlHelper;
import com.example.administrator.cnmar.helper.VolleyHelper;
import com.google.zxing.Result;

import java.io.IOException;
import java.lang.reflect.Field;

import component.half.model.HalfInOrderSpace;
import component.material.model.MaterialInOrderSpace;
import component.product.model.ProductInOrderSpace;
import zxing.camera.CameraManager;
import zxing.decode.DecodeThread;
import zxing.utils.BeepManager;
import zxing.utils.CaptureActivityHandler;
import zxing.utils.InactivityTimer;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = CaptureActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private RelativeLayout goOnScanView;
    private Button btnStop, btnGoOnScan;
    private TextView tvCode;
    private ImageView scanLine;
    private LinearLayout llReturn;
    //  判断是哪个页面启动的扫描页面
    private int FLAG = 0;
    private int id;  //三种出库单id
    private int receiveId, processId, stationId;//料框扫描传递过来的参数
    private int testBoxId;//待检验料框id

    private Rect mCropRect = null;
    private boolean isHasSurface = false;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);


//        获取原料、成品或者半成品出库页面传递过来的出库单id（避免扫描到其他单据）
        id = getIntent().getIntExtra("id", 0);
//        获取标志位FLAG
        FLAG = getIntent().getIntExtra("FLAG", 0);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        goOnScanView = (RelativeLayout) findViewById(R.id.rlMultiScan);

        btnStop = (Button) findViewById(R.id.stopScan);
        btnGoOnScan = (Button) findViewById(R.id.goOnScan);
        tvCode = (TextView) findViewById(R.id.tvCode);

        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        llReturn = (LinearLayout) findViewById(R.id.left_arrow);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (FLAG == 1) {
                Intent intent = new Intent(CaptureActivity.this, MaterialInOrderDetailActivity.class);
                setResult(3, intent);
                CaptureActivity.this.finish();
            } else if (FLAG == 2) {
                Intent intent = new Intent(CaptureActivity.this, MaterialOutOrderDetailActivity.class);
                setResult(4, intent);
                CaptureActivity.this.finish();
            } else if (FLAG == 3) {
                Intent intent = new Intent(CaptureActivity.this, ProductInOrderDetailActivity.class);
                setResult(5, intent);
                CaptureActivity.this.finish();
            } else if (FLAG == 4) {
                Intent intent = new Intent(CaptureActivity.this, ProductOutOrderDetailActivity.class);
                setResult(6, intent);
                CaptureActivity.this.finish();
            } else if (FLAG == 5) {
                Intent intent = new Intent(CaptureActivity.this, HalfProductInOrderDetailActivity.class);
                setResult(7, intent);
                CaptureActivity.this.finish();
            } else if (FLAG == 6) {
                Intent intent = new Intent(CaptureActivity.this, HalfProductOutOrderDetailActivity.class);
                setResult(8, intent);
                CaptureActivity.this.finish();
            }
            //            返回到追溯页面
            else if (FLAG == 100) {
                Intent intent = new Intent(CaptureActivity.this, QualityControlActivity.class);
                setResult(1, intent);
                CaptureActivity.this.finish();
            }
            //            返回到机床扫描页面
            else if (FLAG == -100) {
                Intent intent = new Intent(CaptureActivity.this, ProduceManageActivity.class);
                setResult(1, intent);
                CaptureActivity.this.finish();
            }
            //            返回机床对应的（子）加工单详情页面
            else if (FLAG == 50 || FLAG == 666) {
                CaptureActivity.this.finish();
            }
            //            返回到待检验料框扫描页面
            else if (FLAG == -50) {
                Intent intent = new Intent(CaptureActivity.this, ProduceManageActivity.class);
                setResult(1, intent);
                CaptureActivity.this.finish();
            }
            //            返回到待检验料框扫描页面
            else if (FLAG == -60) {
                Intent intent = new Intent(CaptureActivity.this, ProduceManageActivity.class);
                setResult(1, intent);
                CaptureActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        Intent resultIntent = new Intent();
        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        bundle.putString("result", rawResult.getText());
        Log.d("result", rawResult.getText());
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
//      得到扫描二维码返回的结果
        String result = rawResult.getText();
//      对返回结果进行处理后，赋值给strUrl
        String strUrl = "";

//      扫描结果前加上URL_BASE
        result = UrlHelper.URL_BASE + result;

//      处理原料出库、成品出库以及半成品出库扫描结果,将二维码返回的结果加上单据id后发送请求
//      处理原料入库、成品入库以及半成品入库扫描结果，直接用二维码返回的结果发送请求
//      处理原料、成品追溯的，要将二维码返回的结果做replace("qrcode", "back")操作
//      扫描机床的时候，要在二维码返回的结果后面加上userId字段
//      扫描料框的时候，要在二维码返回的结果后面加上stationId、receiveId字段
//      扫描待检验料框的时候，要在二维码返回的结果后面加上“op=test”字符串
//      扫描不合格品料框的时候，要在二维码返回的结果后面加上“op=fail&testBoxId=”

        if (FLAG == 2 || FLAG == 4 || FLAG == 6) {
            strUrl = result + "?outOrderId=" + String.valueOf(id);
            Log.d("strUrl", strUrl);
        } else if (FLAG == 1 || FLAG == 3 || FLAG == 5) {
            strUrl = result;
        } else if (FLAG == 100) {
            strUrl = result.replace("qrcode", "back");
        } else if (FLAG == -100) {
            strUrl = result + "?userId=" + SPHelper.getInt(this, "userId");
        } else if (FLAG == 50) {
            receiveId = getIntent().getIntExtra("receiveId", 0);
            processId = getIntent().getIntExtra("processId", 0);
            stationId = getIntent().getIntExtra("stationId", 0);
            strUrl = result + "?stationId=" + stationId
                    + "&processId=" + processId
                    + "&receiveId=" + receiveId;
            Log.d("strUrl", strUrl);
//            如果扫描料框的时候，扫到了机台，此处进行判断
            if (!strUrl.contains(UrlHelper.URL_SCANN_BOX)) {
                Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
                CaptureActivity.this.finish();
            }
        } else if (FLAG == -50) {
            strUrl = result + "?op=test";
            Log.d("strUrl", strUrl);
        } else if (FLAG == -60) {
            testBoxId = getIntent().getIntExtra("testBoxId", 0);
            strUrl = result + "?op=fail&testBoxId=" + testBoxId;
            Log.d("strUrl", strUrl);
        } else if (FLAG == 666) {
            receiveId = getIntent().getIntExtra("receiveId", 0);
            processId = getIntent().getIntExtra("processId", 0);
            stationId = getIntent().getIntExtra("stationId", 0);
            strUrl = result + "?op=prev&receiveId=" + receiveId
                    + "&processId=" + processId
                    + "&stationId=" + stationId;
            Log.d("strUrl", strUrl);
//            如果扫描料框的时候，扫到了机台，此处进行判断
            if (!strUrl.contains(UrlHelper.URL_SCANN_BOX)) {
                Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
                CaptureActivity.this.finish();
            }
        }

        //        处理原料出入库扫描
        if (strUrl.contains(UrlHelper.URL_MATERIAL_SCAN)) {
            scanCropView.setVisibility(View.INVISIBLE);
            goOnScanView.setVisibility(View.VISIBLE);
            getMaterialInfoFromNet(strUrl);
        }
        //        处理成品出入库扫描
        else if (strUrl.contains(UrlHelper.URL_PRODUCT_SCAN)) {
            scanCropView.setVisibility(View.INVISIBLE);
            goOnScanView.setVisibility(View.VISIBLE);
            getProductInfoFromNet(strUrl);
        }
        //        处理半成品出入库扫描
        else if (strUrl.contains(UrlHelper.URL_HALF_PRODUCT_SCAN)) {
            scanCropView.setVisibility(View.INVISIBLE);
            goOnScanView.setVisibility(View.VISIBLE);
            getHalfProductInfoFromNet(strUrl);
        }
        //        处理原料追溯扫描
        else if (strUrl.contains(UrlHelper.URL_MATERIAL_TRACE_BACK_SCAN)) {
            Intent intent = new Intent(this, QualityControlActivity.class);
            intent.putExtra("result", strUrl);
            setResult(0, intent);
            CaptureActivity.this.finish();
        }
        //        处理成品追溯扫描
        else if (strUrl.contains(UrlHelper.URL_PRODUCT_TRACE_BACK_SCAN)) {
            Intent intent = new Intent(this, QualityControlActivity.class);
            intent.putExtra("result", strUrl);
            setResult(0, intent);
            CaptureActivity.this.finish();
        }
        //        处理机台扫描
        else if (strUrl.contains(UrlHelper.URL_SCANN_STATION)) {
            Intent intent = new Intent(this, ProduceManageActivity.class);
            intent.putExtra("result", strUrl);
            setResult(0, intent);
            CaptureActivity.this.finish();
        }
        //        处理料框扫描
        else if (strUrl.contains(UrlHelper.URL_SCANN_BOX)
                && !strUrl.contains("op=test") && !strUrl.contains("op=fail") && !strUrl.contains("op=prev")) {
            canInBox(strUrl);//判断是否可以跳转到入料框界面
        }
        //        处理待检验料框扫描
        else if (strUrl.contains(UrlHelper.URL_SCANN_BOX) && strUrl.contains("op=test")) {
            Intent intent = new Intent(this, ProduceManageActivity.class);
            intent.putExtra("test", 2);
            intent.putExtra("result", strUrl);
            setResult(0, intent);
            CaptureActivity.this.finish();
        }
        //        处理不合格品料框扫描
        else if (strUrl.contains(UrlHelper.URL_SCANN_BOX) && strUrl.contains("op=fail")) {
            canFailInBox(strUrl);//判断是否可以跳转到不合格品入料框界面
        }
        //        处理上工序合格品料框扫描
        else if (strUrl.contains(UrlHelper.URL_SCANN_BOX) && strUrl.contains("op=prev")) {
            canOutBox(strUrl);//判断是否可以跳转到上工序合格品出料框界面
        }

        btnGoOnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCropView.setVisibility(View.VISIBLE);
                goOnScanView.setVisibility(View.INVISIBLE);
//               实现连扫
                if (handler != null)
                    handler.restartPreviewAndDecode();

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FLAG == 1) {
                    Intent intent = new Intent(CaptureActivity.this, MaterialInOrderDetailActivity.class);
                    setResult(3, intent);
                    CaptureActivity.this.finish();
                } else if (FLAG == 2) {
                    Intent intent = new Intent(CaptureActivity.this, MaterialOutOrderDetailActivity.class);
                    setResult(4, intent);
                    CaptureActivity.this.finish();
                } else if (FLAG == 3) {
                    Intent intent = new Intent(CaptureActivity.this, ProductInOrderDetailActivity.class);
                    setResult(5, intent);
                    CaptureActivity.this.finish();
                } else if (FLAG == 4) {
                    Intent intent = new Intent(CaptureActivity.this, ProductOutOrderDetailActivity.class);
                    setResult(6, intent);
                    CaptureActivity.this.finish();
                } else if (FLAG == 5) {
                    Intent intent = new Intent(CaptureActivity.this, HalfProductInOrderDetailActivity.class);
                    setResult(7, intent);
                    CaptureActivity.this.finish();
                } else if (FLAG == 6) {
                    Intent intent = new Intent(CaptureActivity.this, HalfProductOutOrderDetailActivity.class);
                    setResult(8, intent);
                    CaptureActivity.this.finish();
                }
            }
        });


    }


    public void getMaterialInfoFromNet(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json = VolleyHelper.getJson(s);
                Log.d("response", json);

                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
//                扫描成功的时候显示条码相关信息
                if (response.isStatus()) {
//                    出入库扫描结果都转化为MaterialInOrderSpace对象
                    MaterialInOrderSpace materialInOrderSpace = JSON.parseObject(response.getData().toString(), MaterialInOrderSpace.class);

                    StringBuilder sb = new StringBuilder();
//                    处理入库扫描
                    if (materialInOrderSpace.getInOrOut().equals("in")) {
                        tvCode.setText(sb.append("原料编码: ")
                                .append(materialInOrderSpace.getMaterial().getCode())
                                .append("\n")
                                .append("仓位编码：")
                                .append(materialInOrderSpace.getSpace().getCode())
                                .append("\n")
                                .append("待入库数量: ")
                                .append(materialInOrderSpace.getPreInStock())
                                .append("\n")
                                .append("已入库数量: ")
                                .append(materialInOrderSpace.getInStock())
                                .toString()
                        );
                    }
//                    处理出库扫描
                    else {
                        tvCode.setText(sb.append("原料编码: ")
                                .append(materialInOrderSpace.getMaterial().getCode())
                                .append("\n")
                                .append("仓位编码：")
                                .append(materialInOrderSpace.getSpace().getCode())
                                .append("\n")
                                .append("待出库数量: ")
                                .append(materialInOrderSpace.getPreInStock())
                                .append("\n")
                                .append("已出库数量: ")
                                .append(materialInOrderSpace.getInStock())
                                .toString()
                        );

                    }

                    tvCode.setTextSize(18);

                } else {
                    tvCode.setText("");
                    MaterialInOrderSpace materialInOrderSpace = JSON.parseObject(response.getData().toString(), MaterialInOrderSpace.class);
                    if (materialInOrderSpace.getInOrOut().equals("in")) {
                        Toast.makeText(CaptureActivity.this, "入库单" + response.getMsg(), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(CaptureActivity.this, "出库单" + response.getMsg(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    public void getProductInfoFromNet(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json = VolleyHelper.getJson(s);
                Log.d("responsere", json);

                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
//                扫描成功的时候显示条码相关信息
                if (response.isStatus()) {

//                    出入库扫描结果都转化为ProductInOrderSpace对象
                    ProductInOrderSpace productInOrderSpace = JSON.parseObject(response.getData().toString(), ProductInOrderSpace.class);

                    StringBuilder sb = new StringBuilder();
//                    处理入库扫描
                    if (productInOrderSpace.getInOrOut().equals("in")) {
                        tvCode.setText(sb.append("成品编码: ")
                                .append(productInOrderSpace.getProduct().getCode())
                                .append("\n")
                                .append("仓位编码：")
                                .append(productInOrderSpace.getSpace().getCode())
                                .append("\n")
                                .append("待入库数量: ")
                                .append(productInOrderSpace.getPreInStock())
                                .append("\n")
                                .append("已入库数量: ")
                                .append(productInOrderSpace.getInStock())
                                .toString()
                        );
                    }
//                    处理出库扫描
                    else {
                        tvCode.setText(sb.append("成品编码: ")
                                .append(productInOrderSpace.getProduct().getCode())
                                .append("\n")
                                .append("仓位编码：")
                                .append(productInOrderSpace.getSpace().getCode())
                                .append("\n")
                                .append("待出库数量: ")
                                .append(productInOrderSpace.getPreInStock())
                                .append("\n")
                                .append("已出库数量: ")
                                .append(productInOrderSpace.getInStock())
                                .toString()
                        );

                    }

                    tvCode.setTextSize(18);

                } else {
                    tvCode.setText("");
                    ProductInOrderSpace productInOrderSpace = JSON.parseObject(response.getData().toString(), ProductInOrderSpace.class);
                    if (productInOrderSpace.getInOrOut().equals("in")) {
                        Toast.makeText(CaptureActivity.this, "入库单" + response.getMsg(), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(CaptureActivity.this, "出库单" + response.getMsg(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    public void getHalfProductInfoFromNet(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json = VolleyHelper.getJson(s);
                Log.d("responsere", json);

                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
//                扫描成功的时候显示条码相关信息
                if (response.isStatus()) {

//                    出入库扫描结果都转化为ProductInOrderSpace对象
                    HalfInOrderSpace halfInOrderSpace = JSON.parseObject(response.getData().toString(), HalfInOrderSpace.class);

                    StringBuilder sb = new StringBuilder();
//                    处理入库扫描
                    if (halfInOrderSpace.getInOrOut().equals("in")) {
                        tvCode.setText(sb.append("半成品编码: ")
                                .append(halfInOrderSpace.getHalf().getCode())
                                .append("\n")
                                .append("仓位编码：")
                                .append(halfInOrderSpace.getSpace().getCode())
                                .append("\n")
                                .append("待入库数量: ")
                                .append(halfInOrderSpace.getPreInStock())
                                .append("\n")
                                .append("已入库数量: ")
                                .append(halfInOrderSpace.getInStock())
                                .toString()
                        );
                    }
//                    处理出库扫描
                    else {
                        tvCode.setText(sb.append("半成品编码: ")
                                .append(halfInOrderSpace.getHalf().getCode())
                                .append("\n")
                                .append("仓位编码：")
                                .append(halfInOrderSpace.getSpace().getCode())
                                .append("\n")
                                .append("待出库数量: ")
                                .append(halfInOrderSpace.getPreInStock())
                                .append("\n")
                                .append("已出库数量: ")
                                .append(halfInOrderSpace.getInStock())
                                .toString()
                        );

                    }

                    tvCode.setTextSize(18);

                } else {
                    tvCode.setText("");
                    HalfInOrderSpace halfInOrderSpace = JSON.parseObject(response.getData().toString(), HalfInOrderSpace.class);
                    if (halfInOrderSpace.getInOrOut().equals("in")) {
                        Toast.makeText(CaptureActivity.this, "入库单" + response.getMsg(), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(CaptureActivity.this, "出库单" + response.getMsg(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    public void canInBox(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json = VolleyHelper.getJson(s);
                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
//                状态成功的时候跳转页面
                if (response.isStatus()) {
                    Intent intent = new Intent(CaptureActivity.this, InBoxActivity.class);
                    intent.putExtra("result", url);
                    intent.putExtra("receiveId", receiveId);
                    intent.putExtra("processId", processId);
                    intent.putExtra("stationId", stationId);
                    startActivity(intent);
                    CaptureActivity.this.finish();
                } else {
                    Toast.makeText(CaptureActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    CaptureActivity.this.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    public void canFailInBox(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json = VolleyHelper.getJson(s);
                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
//                状态成功的时候跳转页面
                if (response.isStatus()) {
                    Intent intent = new Intent(CaptureActivity.this, FailInBoxActivity.class);
                    intent.putExtra("result", url);
                    startActivity(intent);
                    CaptureActivity.this.finish();
                } else {
                    Toast.makeText(CaptureActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    CaptureActivity.this.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    public void canOutBox(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String json = VolleyHelper.getJson(s);
                component.common.model.Response response = JSON.parseObject(json, component.common.model.Response.class);
//                状态成功的时候跳转页面
                if (response.isStatus()) {
                    Intent intent = new Intent(CaptureActivity.this, PrevProcessOutBoxActivity.class);
                    intent.putExtra("result", url);
                    startActivity(intent);
                    CaptureActivity.this.finish();
                } else {
                    Toast.makeText(CaptureActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    CaptureActivity.this.finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}