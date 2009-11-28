package net.java.jveez.utils;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import org.apache.log4j.Logger;

public final class BlurUtils
{
    private static final Logger LOG = Logger.getLogger( BlurUtils.class );

    private static final GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    private BlurUtils()
    {
    }

    public static BufferedImage createDefaultBlur( final BufferedImage sourceImage, final int blurSize )
    {
        final Kernel blurKernel = new Kernel( 5, 5, new float[]{
                3.0f / 1003.0f, 13.0f / 1003.0f, 22.0f / 1003.0f, 13.0f / 1003.0f, 3.0f / 1003.0f,
                13.0f / 1003.0f, 59.0f / 1003.0f, 97.0f / 1003.0f, 59.0f / 1003.0f, 13.0f / 1003.0f,
                22.0f / 1003.0f, 97.0f / 1003.0f, 159.0f / 1003.0f, 97.0f / 1003.0f, 22.0f / 1003.0f,
                13.0f / 1003.0f, 59.0f / 1003.0f, 97.0f / 1003.0f, 59.0f / 1003.0f, 13.0f / 1003.0f,
                3.0f / 1003.0f, 13.0f / 1003.0f, 22.0f / 1003.0f, 13.0f / 1003.0f, 3.0f / 1003.0f,
        } );
//    Kernel blurKernel = new Kernel(5, 5, new float[]{
//      0, 0, 1, 0, 0,
//      0, 0, 1, 0, 0,
//      1, 1, 1, 1, 1,
//      0, 0, 1, 0, 0,
//      0, 0, 1, 0, 0
//    });
        final BufferedImage destImage = BlurUtils.createCompatibleImage( sourceImage.getWidth() + 5, sourceImage.getHeight() + 5 );
        final ConvolveOp convolveOp = new ConvolveOp( blurKernel );
        convolveOp.filter( sourceImage, destImage );
        return destImage;
    }

    public static BufferedImage createGaussianBlur( final BufferedImage sourceImage, final int radius )
    {
        final double variance = 1.5;
        final int blurSize = radius + radius + 1;

        final float[] data = new float[blurSize * blurSize];
        float sum = 0;
        for ( int i = -radius; i < radius; i++ )
        {
            for ( int j = -radius; j < radius; j++ )
            {
                final int udx = ( i + radius ) * blurSize + j + radius;
                final float v = (float) ( Math.exp( -( i * i + j * j ) / ( 2 * variance * variance ) ) / Math.sqrt( 2 * Math.PI * variance ) );
                data[udx] = v;
                sum += v;
            }
        }

        for ( int i = 0; i < data.length; i++ )
        {
            data[i] = data[i] / sum;
        }

        final Kernel blurKernel = new Kernel( blurSize, blurSize, data );

        final BufferedImage destImage = BlurUtils.createCompatibleImage( sourceImage.getWidth() + blurSize, sourceImage.getHeight() + blurSize );
        final ConvolveOp convolveOp = new ConvolveOp( blurKernel, ConvolveOp.EDGE_NO_OP, null );
        convolveOp.filter( sourceImage, destImage );
        return destImage;
    }

    public static BufferedImage createGaussianBlur2( final BufferedImage sourceImage, final BufferedImage destImage, final int radius )
    {
        final double variance = 1.5;
        final int blurSize = radius + radius + 1;

        final float[] data = new float[blurSize * blurSize];
        float sum = 0;
        for ( int i = -radius; i < radius; i++ )
        {
            for ( int j = -radius; j < radius; j++ )
            {
                final int udx = ( i + radius ) * blurSize + j + radius;
                final float v = (float) ( Math.exp( -( i * i + j * j ) / ( 2 * variance * variance ) ) / Math.sqrt( 2 * Math.PI * variance ) );
                data[udx] = v;
                sum += v;
            }
        }

        for ( int i = 0; i < data.length; i++ )
        {
            data[i] = data[i] / sum;
        }

        final Kernel blurKernel = new Kernel( blurSize, blurSize, data );

//    BufferedImage destImage = BlurUtils.createCompatibleImage(sourceImage.getWidth() + blurSize, sourceImage.getHeight() + blurSize);
        final ConvolveOp convolveOp = new ConvolveOp( blurKernel, ConvolveOp.EDGE_NO_OP, null );
        convolveOp.filter( sourceImage, destImage );
        return destImage;
    }

    /**
     * Create an image containing the 'shadow' of the provided source image. <i>Note that the resulting image will be
     * bigger than the original one.</i> <p/> todo : Refactor pixel access code to use ImageIterator instead.
     *
     * @param src           source image
     * @param shadowColor   color of the shadow
     * @param shadowSize    'size' of the blur effect
     * @param shadowOpacity opacity of the final shadow
     * @return an image containing the shadow of the input image
     */
    public static BufferedImage createShadow( final BufferedImage src, final Color shadowColor, final int shadowSize, final float shadowOpacity )
    {
        final int srcWidth = src.getWidth();
        final int srcHeight = src.getHeight();

        final int dstWidth = srcWidth + shadowSize;
        final int dstHeight = srcHeight + shadowSize;

        final int left = ( shadowSize - 1 ) >> 1;
        final int right = shadowSize - left;

        final int yStop = dstHeight - right;

        final BufferedImage dst = new BufferedImage( dstWidth, dstHeight, BufferedImage.TYPE_INT_ARGB );

        final int shadowRgb = shadowColor.getRGB() & 0x00FFFFFF;

        final int[] aHistory = new int[shadowSize];
        int historyIdx;

        int aSum;

        final ColorModel srcColorModel = src.getColorModel();
        final WritableRaster srcRaster = src.getRaster();
        final int[] dstBuffer = ( (DataBufferInt) dst.getRaster().getDataBuffer() ).getData();

        final int lastPixelOffset = right * dstWidth;
        final float hSumDivider = 1.0f / shadowSize;
        final float vSumDivider = shadowOpacity / shadowSize;

        // horizontal pass : extract the alpha mask from the source picture and blur it into the destination picture
        for ( int srcY = 0, dstOffset = left * dstWidth; srcY < srcHeight; srcY++ )
        {
            // first pixels are empty
            for ( historyIdx = 0; historyIdx < shadowSize; )
            {
                aHistory[historyIdx++] = 0;
            }

            aSum = 0;
            historyIdx = 0;

            // compute the blur average with pixels from the source image
            for ( int srcX = 0; srcX < srcWidth; srcX++ )
            {
                int a = (int) ( aSum * hSumDivider );     // calculate alpha value
                dstBuffer[dstOffset++] = a << 24;       // store the alpha value only - the shadow color will be added in the next pass

                aSum -= aHistory[historyIdx]; // substract the oldest pixel from the sum

                // extract the new pixel ...
                a = srcColorModel.getAlpha( srcRaster.getDataElements( srcX, srcY, null ) ); // todo : find a faster way to read the alpha value
                aHistory[historyIdx] = a;   // ... and store its value into history
                aSum += a;                  // ... and add its value to the sum

                if ( ++historyIdx >= shadowSize )
                {
                    historyIdx -= shadowSize;
                }
            }

            // blur the end of the row - no new pixels to grab
            for ( int i = 0; i < shadowSize; i++ )
            {
                final int a = (int) ( aSum * hSumDivider );
                dstBuffer[dstOffset++] = a << 24;

                // substract the oldest pixel from the sum ... and nothing new to add !
                aSum -= aHistory[historyIdx];

                if ( ++historyIdx >= shadowSize )
                {
                    historyIdx -= shadowSize;
                }
            }
        }

        // vertical pass
        for ( int x = 0, bufferOffset = 0; x < dstWidth; x++, bufferOffset = x )
        {
            aSum = 0;

            // first pixels are empty
            for ( historyIdx = 0; historyIdx < left; )
            {
                aHistory[historyIdx++] = 0;
            }

            // and then they come from the dstBuffer
            for ( int y = 0; y < right; y++, bufferOffset += dstWidth )
            {
                final int a = dstBuffer[bufferOffset] >>> 24;         // extract alpha
                aHistory[historyIdx++] = a;                     // store into history
                aSum += a;                                      // and add to sum
            }

            bufferOffset = x;
            historyIdx = 0;

            // compute the blur average with pixels from the previous pass
            for ( int y = 0; y < yStop; y++, bufferOffset += dstWidth )
            {
                int a = (int) ( aSum * vSumDivider );             // calculate alpha value
                dstBuffer[bufferOffset] = a << 24 | shadowRgb;  // store alpha value + shadow color

                aSum -= aHistory[historyIdx];   // substract the oldest pixel from the sum

                a = dstBuffer[bufferOffset + lastPixelOffset] >>> 24;   // extract the new pixel ...
                aHistory[historyIdx] = a;                               // ... and store its value into history
                aSum += a;                                              // ... and add its value to the sum

                if ( ++historyIdx >= shadowSize )
                {
                    historyIdx -= shadowSize;
                }
            }

            // blur the end of the column - no pixels to grab anymore
            for ( int y = yStop; y < dstHeight; y++, bufferOffset += dstWidth )
            {
                final int a = (int) ( aSum * vSumDivider );
                dstBuffer[bufferOffset] = a << 24 | shadowRgb;

                aSum -= aHistory[historyIdx];   // substract the oldest pixel from the sum

                if ( ++historyIdx >= shadowSize )
                {
                    historyIdx -= shadowSize;
                }
            }
        }

        return dst;
    }

    /**
     * Blur the provided source image into a new image. <i>Note that the resulting image will be bigger than the
     * original one.</i>
     *
     * @param src        source image
     * @param shadowSize blur size
     * @return an image containing the blurred source image
     */
    public static BufferedImage blur( final BufferedImage src, final int shadowSize )
    {
        final int srcWidth = src.getWidth();
        final int srcHeight = src.getHeight();

        final int dstWidth = srcWidth + shadowSize;
        final int dstHeight = srcHeight + shadowSize;

        final BufferedImage dst = new BufferedImage( dstWidth, dstHeight, BufferedImage.TYPE_INT_ARGB );

        final int left = ( shadowSize - 1 ) >> 1;
        final int right = shadowSize - left;
        final int xStop = dstWidth - right;
        final int yStop = dstHeight - right;

        final Graphics2D g = dst.createGraphics();
        g.drawImage( src, left, left, null );
        g.dispose();

        final int[] aHistory = new int[shadowSize];
        final int[] rHistory = new int[shadowSize];
        final int[] vHistory = new int[shadowSize];
        final int[] bHistory = new int[shadowSize];
        int historyIdx;

        int aSum;
        int rSum;
        int vSum;
        int bSum;

        final float hSumDivider = 1.0f / shadowSize;
        final float vSumDivider = 1.0f / shadowSize;

        // horizontal pass
        for ( int y = left; y < yStop; y++ )
        {
            aSum = 0;
            rSum = 0;
            vSum = 0;
            bSum = 0;

            for ( historyIdx = 0; historyIdx < left; historyIdx++ )
            {
                aHistory[historyIdx] = 0;
                rHistory[historyIdx] = 0;
                vHistory[historyIdx] = 0;
                bHistory[historyIdx] = 0;
            }

            for ( int x = 0; x < right; x++, historyIdx++ )
            {
                final int argb = dst.getRGB( x, y );
                final int a = argb >>> 24;
                final int r = ( argb & 0x00FF0000 ) >>> 16;
                final int v = ( argb & 0x0000FF00 ) >>> 8;
                final int b = ( argb & 0x000000FF );
                aHistory[historyIdx] = a;
                rHistory[historyIdx] = r;
                vHistory[historyIdx] = v;
                bHistory[historyIdx] = b;
                aSum += a;
                rSum += r;
                vSum += v;
                bSum += b;
            }

            historyIdx = 0;

            for ( int x = 0; x < xStop; x++ )
            {
                int a = (int) ( aSum * hSumDivider );
                int r = (int) ( rSum * hSumDivider );
                int v = (int) ( vSum * hSumDivider );
                int b = (int) ( bSum * hSumDivider );
                dst.setRGB( x, y, a << 24 | r << 16 | v << 8 | b );

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];
                rSum -= rHistory[historyIdx];
                vSum -= vHistory[historyIdx];
                bSum -= bHistory[historyIdx];

                // get the lastest pixel
                final int argb = dst.getRGB( x + right, y );
                a = argb >>> 24;
                r = ( argb & 0x00FF0000 ) >>> 16;
                v = ( argb & 0x0000FF00 ) >>> 8;
                b = ( argb & 0x000000FF );
                aHistory[historyIdx] = a;
                rHistory[historyIdx] = r;
                vHistory[historyIdx] = v;
                bHistory[historyIdx] = b;
                aSum += a;
                rSum += r;
                vSum += v;
                bSum += b;

                if ( ++historyIdx >= shadowSize )
                {
                    historyIdx -= shadowSize;
                }
            }

            for ( int x = xStop; x < dstWidth; x++ )
            {
                final int a = (int) ( aSum * hSumDivider );
                final int r = (int) ( rSum * hSumDivider );
                final int v = (int) ( vSum * hSumDivider );
                final int b = (int) ( bSum * hSumDivider );
                dst.setRGB( x, y, a << 24 | r << 16 | v << 8 | b );

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];
                rSum -= rHistory[historyIdx];
                vSum -= vHistory[historyIdx];
                bSum -= bHistory[historyIdx];

                if ( ++historyIdx >= shadowSize )
                {
                    historyIdx -= shadowSize;
                }
            }

        }

        // vertical pass
        for ( int x = 0; x < dstWidth; x++ )
        {
            aSum = 0;
            rSum = 0;
            vSum = 0;
            bSum = 0;

            for ( historyIdx = 0; historyIdx < left; historyIdx++ )
            {
                aHistory[historyIdx] = 0;
                rHistory[historyIdx] = 0;
                vHistory[historyIdx] = 0;
                bHistory[historyIdx] = 0;
            }

            for ( int y = 0; y < right; y++, historyIdx++ )
            {
                final int argb = dst.getRGB( x, y );
                final int a = argb >>> 24;
                final int r = ( argb & 0x00FF0000 ) >>> 16;
                final int v = ( argb & 0x0000FF00 ) >>> 8;
                final int b = ( argb & 0x000000FF );
                aHistory[historyIdx] = a;
                rHistory[historyIdx] = r;
                vHistory[historyIdx] = v;
                bHistory[historyIdx] = b;
                aSum += a;
                rSum += r;
                vSum += v;
                bSum += b;
            }

            historyIdx = 0;

            for ( int y = 0; y < yStop; y++ )
            {
                int a = (int) ( aSum * vSumDivider );
                int r = (int) ( rSum * vSumDivider );
                int v = (int) ( vSum * vSumDivider );
                int b = (int) ( bSum * vSumDivider );
                dst.setRGB( x, y, a << 24 | r << 16 | v << 8 | b );

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];
                rSum -= rHistory[historyIdx];
                vSum -= vHistory[historyIdx];
                bSum -= bHistory[historyIdx];

                // get the lastest pixel
                final int argb = dst.getRGB( x, y + right );
                a = argb >>> 24;
                r = ( argb & 0x00FF0000 ) >>> 16;
                v = ( argb & 0x0000FF00 ) >>> 8;
                b = ( argb & 0x000000FF );
                aHistory[historyIdx] = a;
                rHistory[historyIdx] = r;
                vHistory[historyIdx] = v;
                bHistory[historyIdx] = b;
                aSum += a;
                rSum += r;
                vSum += v;
                bSum += b;

                if ( ++historyIdx >= shadowSize )
                {
                    historyIdx -= shadowSize;
                }
            }

            for ( int y = yStop; y < dstHeight; y++ )
            {
                final int a = (int) ( aSum * vSumDivider );
                final int r = (int) ( rSum * vSumDivider );
                final int v = (int) ( vSum * vSumDivider );
                final int b = (int) ( bSum * vSumDivider );
                dst.setRGB( x, y, a << 24 | r << 16 | v << 8 | b );

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];
                rSum -= rHistory[historyIdx];
                vSum -= vHistory[historyIdx];
                bSum -= bHistory[historyIdx];

                if ( ++historyIdx >= shadowSize )
                {
                    historyIdx -= shadowSize;
                }
            }
        }

        return dst;
    }

    /**
     * Blur the provided source image into a new image. <i>Note that the resulting image will have the same dimension as
     * the original one.</i> <i>This version is a bit more OO oriented ... without speed penality.</i>
     *
     * @param srcBufferedImage source image
     * @param dstBufferedImage destination image
     * @param blurSize         blur size
     * @return an image containing the blurred source image
     */
    public static BufferedImage blur2( final BufferedImage srcBufferedImage, final BufferedImage dstBufferedImage, final int blurSize )
    {
        final int width = srcBufferedImage.getWidth();
        final int height = srcBufferedImage.getHeight();
        final float sumDivider = 1.0f / blurSize;
        final int left = ( blurSize - 1 ) >> 1;
        final int right = blurSize - left;

        final int xStop = width - right;
        final int yStop = height - right;

        final ImageIterator srcImg = ImageIterator.iterator( srcBufferedImage );
        final ImageIterator dstImg = ImageIterator.iterator( dstBufferedImage );
        final Pixel p = new Pixel();

        final Pixel[] pHistory = new Pixel[blurSize];
        for ( int i = 0; i < pHistory.length; i++ )
        {
            pHistory[i] = new Pixel();
        }

        final Pixel pSum = new Pixel();

        int historyIdx;

        // horizontal pass
        srcImg.goTo( 0, 0 );
        dstImg.goTo( 0, 0 );
        for ( int y = 0; y < height; y++ )
        {
            pSum.clear();

            // compute fist pixels using symetry
            for ( historyIdx = 0; historyIdx < left; historyIdx++ )
            {
                srcImg.getXrel( p, left - historyIdx );
                pHistory[historyIdx].set( p );
                pSum.add( p );
            }

            for ( int x = 0; x < right; x++, historyIdx++ )
            {
                srcImg.getXrel( p, x );
                pHistory[historyIdx].set( p );
                pSum.add( p );
            }

            historyIdx = 0;

            for ( int x = 0; x < xStop; x++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.setXrel( p, x );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                // get the lastest pixel
                srcImg.getXrel( p, x + right );
                pHistory[historyIdx].set( p );
                pSum.add( p );
                if ( ++historyIdx >= blurSize )
                {
                    historyIdx -= blurSize;
                }
            }

            for ( int x = xStop; x < width; x++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.setXrel( p, x );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                if ( ++historyIdx >= blurSize )
                {
                    historyIdx -= blurSize;
                }
            }

            dstImg.incY();
            srcImg.incY();
        }

        // vertical pass
        for ( int x = 0; x < width; x++ )
        {
            pSum.clear();

            dstImg.goTo( x, left );

            // compute fist pixels using symetry
            for ( historyIdx = 0; historyIdx < left; historyIdx++ )
            {
                dstImg.get( p );
                pHistory[historyIdx].set( p );
                pSum.add( p );
                dstImg.decY();
            }

            for ( int y = 0; y < right; y++, historyIdx++ )
            {
                dstImg.get( p );
                pHistory[historyIdx].set( p );
                pSum.add( p );
                dstImg.incY();
            }

            historyIdx = 0;
            dstImg.goTo( x, 0 );

            for ( int y = 0; y < yStop; y++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.set( p );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                // get the lastest pixel
                dstImg.getYrel( p, right );
                pHistory[historyIdx].set( p );
                pSum.add( p );

                dstImg.incY();

                if ( ++historyIdx >= blurSize )
                {
                    historyIdx -= blurSize;
                }
            }

            for ( int y = yStop; y < height; y++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.set( p );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                dstImg.incY();

                if ( ++historyIdx >= blurSize )
                {
                    historyIdx -= blurSize;
                }
            }
        }

        return dstBufferedImage;
    }

    public static BufferedImage blur2( final BufferedImage srcBufferedImage, final BufferedImage dstBufferedImage, final int blurSize, final Color mix, final float factor )
    {
        final int width = srcBufferedImage.getWidth();
        final int height = srcBufferedImage.getHeight();
        final float sumDivider = 1.0f / blurSize;
        final int left = ( blurSize - 1 ) >> 1;
        final int right = blurSize - left;

        final int mixR = mix.getRed();
        final int mixG = mix.getGreen();
        final int mixB = mix.getBlue();
        final int mixA = mix.getAlpha();

        final int xStop = width - right;
        final int yStop = height - right;

        final ImageIterator srcImg = ImageIterator.iterator( srcBufferedImage );
        final ImageIterator dstImg = ImageIterator.iterator( dstBufferedImage );
        final Pixel p = new Pixel();

        final Pixel[] pHistory = new Pixel[blurSize];
        for ( int i = 0; i < pHistory.length; i++ )
        {
            pHistory[i] = new Pixel();
        }

        final Pixel pSum = new Pixel();

        int historyIdx;

        // horizontal pass
        srcImg.goTo( 0, 0 );
        dstImg.goTo( 0, 0 );
        for ( int y = 0; y < height; y++ )
        {
            pSum.clear();

            // compute fist pixels using symetry
            for ( historyIdx = 0; historyIdx < left; historyIdx++ )
            {
                srcImg.getXrel( p, left - historyIdx );
                pHistory[historyIdx].set( p );
                pSum.add( p );
            }

            for ( int x = 0; x < right; x++, historyIdx++ )
            {
                srcImg.getXrel( p, x );
                pHistory[historyIdx].set( p );
                pSum.add( p );
            }

            historyIdx = 0;

            for ( int x = 0; x < xStop; x++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.setXrel( p, x );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                // get the lastest pixel
                srcImg.getXrel( p, x + right );
                pHistory[historyIdx].set( p );
                pSum.add( p );
                if ( ++historyIdx >= blurSize )
                {
                    historyIdx -= blurSize;
                }
            }

            for ( int x = xStop; x < width; x++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.setXrel( p, x );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                if ( ++historyIdx >= blurSize )
                {
                    historyIdx -= blurSize;
                }
            }

            dstImg.incY();
            srcImg.incY();
        }

        // vertical pass
        for ( int x = 0; x < width; x++ )
        {
            pSum.clear();

            dstImg.goTo( x, left );

            // compute fist pixels using symetry
            for ( historyIdx = 0; historyIdx < left; historyIdx++ )
            {
                dstImg.get( p );
                pHistory[historyIdx].set( p );
                pSum.add( p );
                dstImg.decY();
            }

            for ( int y = 0; y < right; y++, historyIdx++ )
            {
                dstImg.get( p );
                pHistory[historyIdx].set( p );
                pSum.add( p );
                dstImg.incY();
            }

            historyIdx = 0;
            dstImg.goTo( x, 0 );

            for ( int y = 0; y < yStop; y++ )
            {
                pSum.multAndCopy( sumDivider, p );
                p.mix( mixR, mixG, mixB, mixA, factor );
                dstImg.set( p );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                // get the lastest pixel
                dstImg.getYrel( p, right );
                pHistory[historyIdx].set( p );
                pSum.add( p );

                dstImg.incY();

                if ( ++historyIdx >= blurSize )
                {
                    historyIdx -= blurSize;
                }
            }

            for ( int y = yStop; y < height; y++ )
            {
                pSum.multAndCopy( sumDivider, p );
                p.mix( mixR, mixG, mixB, mixA, factor );
                dstImg.set( p );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                dstImg.incY();

                if ( ++historyIdx >= blurSize )
                {
                    historyIdx -= blurSize;
                }
            }
        }

        return dstBufferedImage;
    }

    /**
     * Blur the provided source image into a new image, optimized for a certain blur size (5). <i>Note that the
     * resulting image will have the same dimension as the original one.</i> <i>This version is a bit more OO oriented
     * ... without speed penality.</i> <i>Still need to be optimized.</i>
     *
     * @param srcBufferedImage source image
     * @param dstBufferedImage destination image
     * @return an image containing the blurred source image
     */
    public static BufferedImage blur2( final BufferedImage srcBufferedImage, final BufferedImage dstBufferedImage )
    {
        final int width = srcBufferedImage.getWidth();
        final int height = srcBufferedImage.getHeight();
        final float sumDivider = 1.0f / 5;
        final int left = 2;
        final int right = 3;

        final int xStop = width - right;
        final int yStop = height - right;

        final ImageIterator srcImg = ImageIterator.iterator( srcBufferedImage );
        final ImageIterator dstImg = ImageIterator.iterator( dstBufferedImage );
        final Pixel p = new Pixel();

        final Pixel[] pHistory = new Pixel[5];
        pHistory[0] = new Pixel();
        pHistory[1] = new Pixel();
        pHistory[2] = new Pixel();
        pHistory[3] = new Pixel();
        pHistory[4] = new Pixel();

        final Pixel pSum = new Pixel();

        int historyIdx;

        // horizontal pass
        for ( int y = 0; y < height; y++ )
        {
            srcImg.goTo( 0, y );
            dstImg.goTo( 0, y );
            pSum.clear();

            // compute fist pixels using symetry
            srcImg.getXrel( p, 2 );
            pHistory[0].set( p );
            pSum.add( p );
            pHistory[4].set( p );
            pSum.add( p );
            srcImg.getXrel( p, 1 );
            pHistory[1].set( p );
            pSum.add( p );
            pHistory[3].set( p );
            pSum.add( p );
            srcImg.getXrel( p, 0 );
            pHistory[2].set( p );
            pSum.add( p );

            historyIdx = 0;

            for ( int x = 0; x < xStop; x++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.set( p );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                // get the lastest pixel
                srcImg.getXrel( p, right );
                pHistory[historyIdx].set( p );
                pSum.add( p );

                srcImg.incX();
                dstImg.incX();

                if ( ++historyIdx >= 5 )
                {
                    historyIdx -= 5;
                }
            }

            for ( int x = xStop; x < width; x++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.set( p );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                dstImg.incX();

                if ( ++historyIdx >= 5 )
                {
                    historyIdx -= 5;
                }
            }

            dstImg.incY();
            srcImg.incY();
        }

        // vertical pass
        for ( int x = 0; x < width; x++ )
        {
            pSum.clear();

            dstImg.goTo( x, left );

            // compute fist pixels using symetry
            for ( historyIdx = 0; historyIdx < left; historyIdx++ )
            {
                dstImg.get( p );
                pHistory[historyIdx].set( p );
                pSum.add( p );
                dstImg.decY();
            }

            for ( int y = 0; y < right; y++, historyIdx++ )
            {
                dstImg.get( p );
                pHistory[historyIdx].set( p );
                pSum.add( p );
                dstImg.incY();
            }

            historyIdx = 0;
            dstImg.goTo( x, 0 );

            for ( int y = 0; y < yStop; y++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.set( p );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                // get the lastest pixel
                dstImg.getYrel( p, right );
                pHistory[historyIdx].set( p );
                pSum.add( p );

                dstImg.incY();

                if ( ++historyIdx >= 5 )
                {
                    historyIdx -= 5;
                }
            }

            for ( int y = yStop; y < height; y++ )
            {
                pSum.multAndCopy( sumDivider, p );
                dstImg.set( p );

                // substract the oldest pixel from the sum
                pSum.sub( pHistory[historyIdx] );

                dstImg.incY();

                if ( ++historyIdx >= 5 )
                {
                    historyIdx -= 5;
                }
            }
        }
        return dstBufferedImage;
    }

    public static BufferedImage createCompatibleImage( final int width, final int height )
    {
        return graphicsConfiguration.createCompatibleImage( width, height );
    }

    public static BufferedImage createCompatibleTransluentImage( final int width, final int height )
    {
        return graphicsConfiguration.createCompatibleImage( width, height, Transparency.TRANSLUCENT );
    }

    public static BufferedImage copyIntoTransluentImage( final BufferedImage image )
    {
        final BufferedImage copy = graphicsConfiguration.createCompatibleImage( image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT );
        final Graphics2D g = copy.createGraphics();
        try
        {
            g.drawImage( image, 0, 0, null );
        }
        finally
        {
            g.dispose();
        }
        return copy;
    }

    public static BufferedImage copyIntoCompatibleImage( final BufferedImage image )
    {
        final BufferedImage copy = graphicsConfiguration.createCompatibleImage( image.getWidth(), image.getHeight() );
        final Graphics2D g = copy.createGraphics();
        try
        {
            g.drawImage( image, 0, 0, null );
        }
        finally
        {
            g.dispose();
        }
        return copy;
    }

    public static BufferedImage captureScreen( final int x, final int y, final int width, final int height ) throws AWTException
    {
        return captureScreen( new Rectangle( x, y, width, height ) );
    }

    public static BufferedImage captureScreen( final Rectangle bounds ) throws AWTException
    {
        final Robot robot = new Robot( graphicsConfiguration.getDevice() );
        return robot.createScreenCapture( bounds );
    }

    public static BufferedImage createTestImage( final int width, final int height )
    {
        final BufferedImage sourceImage = createCompatibleTransluentImage( width, height );
        final Graphics2D g = (Graphics2D) sourceImage.getGraphics();
        try
        {
            g.setComposite( AlphaComposite.Clear );
            g.setColor( Color.BLACK );
            g.fillRect( 0, 0, width, height );
            g.setComposite( AlphaComposite.SrcOver );
            g.setColor( new Color( 255, 255, 255, 255 ) );
            g.fillOval( width / 4, height / 4, width / 2, height / 2 );
        }
        finally
        {
            g.dispose();
        }
        return sourceImage;
    }

    /**
     * Convenience method that returns a scaled instance of the provided {@code BufferedImage}.
     *
     * @param img           the original image to be scaled
     * @param targetWidth   the desired width of the scaled instance, in pixels
     * @param targetHeight  the desired height of the scaled instance, in pixels
     * @param hint          one of the rendering hints that corresponds to {@code RenderingHints.KEY_INTERPOLATION}
     *                      (e.g. {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR}, {@code
     *                      RenderingHints.VALUE_INTERPOLATION_BILINEAR}, {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step scaling technique that provides higher quality
     *                      than the usual one-step technique (only useful in downscaling cases, where {@code
     *                      targetWidth} or {@code targetHeight} is smaller than the original dimensions, and generally
     *                      only when the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage} <p/><p/> Origin: Chris Campbell
     *         http://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
     */
    public static BufferedImage createScaledInstance( final BufferedImage img, final int targetWidth, final int targetHeight, final Object hint,
                                                      final boolean higherQuality )
    {
        LOG.debug( "creating " + ( higherQuality ? "high" : "low" ) + " quality scaled image" );
        final int type = ( img.getTransparency() == Transparency.OPAQUE ) ? BufferedImage.TYPE_INT_RGB :
                BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if ( higherQuality )
        {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        }
        else
        {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do
        {
            if ( higherQuality && w > targetWidth )
            {
                w /= 2;
                if ( w < targetWidth )
                {
                    w = targetWidth;
                }
            }

            if ( higherQuality && h > targetHeight )
            {
                h /= 2;
                if ( h < targetHeight )
                {
                    h = targetHeight;
                }
            }

            final BufferedImage tmp = new BufferedImage( w, h, type );
            final Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint( RenderingHints.KEY_INTERPOLATION, hint );
            g2.drawImage( ret, 0, 0, w, h, null );
            g2.dispose();

            ret = tmp;
        }
        while ( w != targetWidth || h != targetHeight );

        return ret;
    }
}
