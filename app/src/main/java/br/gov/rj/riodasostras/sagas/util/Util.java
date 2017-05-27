package br.gov.rj.riodasostras.sagas.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Date;

/**
 * Created by alex on 08/02/17.
 */

public class Util {
    public Bitmap getCircleBitmap(Bitmap pBitmap) {
        final Bitmap retorno = Bitmap.createBitmap(pBitmap.getWidth(), pBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(retorno);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, pBitmap.getWidth(), pBitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(Color.RED);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(pBitmap, rect, rect, paint);

        return retorno;
    }

    public String getDiferencaDatas(Date pData1, Date pData2) {
        long vDiferenca = pData2.getTime() - pData1.getTime();

        long vSegundo = 1000;
        long vMinuto = vSegundo * 60;
        long vHora = vMinuto * 60;
        long vDia = vHora * 24;

        long vDiasPassados = vDiferenca / vDia;
        if (vDiasPassados > 0) return String.valueOf(vDiasPassados) + "d";
        else {
            long vHorasPassadas = vDiferenca / vHora;
            if (vHorasPassadas > 0) return String.valueOf(vHorasPassadas) + "h";
            else {
                long vMinutosPassados = vDiferenca / vMinuto;
                if (vMinutosPassados > 0) return String.valueOf(vMinutosPassados) + "m";
                else {
                    long vSegundosPassados = vDiferenca / vSegundo;
                    return String.valueOf(vSegundosPassados) + "s";
                }
            }
        }
    }
}