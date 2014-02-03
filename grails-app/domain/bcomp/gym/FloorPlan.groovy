package bcomp.gym

import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream
import java.awt.Image

class FloorPlan {

    static transients = ['image']

    static mapping = { imageAsPng sqlType: 'mediumblob'}

    static belongsTo = [gym: Gym]

    int widthInPx, heightInPx;
    String imageUrl;

}
