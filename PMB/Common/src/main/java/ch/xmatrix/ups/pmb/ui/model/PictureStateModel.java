/* ====================================================================
 *  Copyright 2004-2005 www.xmatrix.ch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 * ====================================================================
 */
package ch.xmatrix.ups.pmb.ui.model;

import static java.lang.Math.round;

/**
 * Model to hold the state concerning the visible bound of the picture. X1, X2, Y1, Y2 passed to or retrieved from this
 * model all lie between 0 and 1 and reflect the percentage of x1, x2 and y1, y2 by the width w and height h,
 * respectively.<p/>
 *
 * <img src="PictureStateModel.png"/><p/>
 *
 * The doubles passed to the setter methods are rounded to 2 positions after the comma.
 *
 * @author Daniel Frey
 * @version $Revision: 1.2 $ $Date: 2007/09/27 10:47:39 $
 */
public class PictureStateModel {

    private final String picture;

    private Integer x1 = 0;
    private Integer x2 = 100;
    private Integer y1 = 0;
    private Integer y2 = 100;

    public PictureStateModel(final String picture) {
        this.picture = picture;
    }

    public PictureStateModel(final PictureStateModel that) {
        this.picture = that.picture;
        this.x1 = that.x1;
        this.x2 = that.x2;
        this.y1 = that.y1;
        this.y2 = that.y2;
    }

    public void setX1(final Double x1) {
        this.x1 = (int) round(100 * x1);
    }

    public Double getX1() {
        return x1 / 100d;
    }

    public void setX2(final Double x2) {
        this.x2 = (int) round(100 * x2);
    }

    public Double getX2() {
        return x2 / 100d;
    }

    public void setY1(final Double y1) {
        this.y1 = (int) round(100 * y1);
    }

    public Double getY1() {
        return y1 / 100d;
    }

    public void setY2(final Double y2) {
        this.y2 = (int) round(100 * y2);
    }

    public Double getY2() {
        return y2 / 100d;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public String toString() {
        return x1 + " " + x2 + " " + y1 + " " + y2;
    }
}
