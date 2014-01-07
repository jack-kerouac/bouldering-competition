package bcomp.gym

import javax.imageio.ImageIO
import javax.imageio.stream.ImageInputStream
import java.awt.Image

class FloorPlan {

    static transients = ['image']

    static mapping = { imageAsPng sqlType: 'blob'}

    static belongsTo = [gym: Gym]

    int widthInPx, heightInPx;
    byte[] imageAsPng;

    public FloorPlan(Image image) {
        setImage(image);
    }

    public void setImage(Image image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        imageAsPng = out.toByteArray();

        widthInPx = image.getWidth()
        heightInPx = image.getHeight()
    }

    public Image getImage() {
        return ImageIO.read(getImageAsInputStream())
    }

    public InputStream getImageAsInputStream() {
        return new ByteArrayInputStream(imageAsPng)
    }

}
