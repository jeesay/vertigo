package vertigo.graphics;

import java.nio.IntBuffer;

public class IBO extends BO {
  
  IntBuffer buffer;
  
  public IBO() {
    super();
  }
  
  public void setIntBuffer(IntBuffer buf) {
        buffer = buf;
        this.type = "INDX";
        this.stride = 0;
    }

    public IntBuffer getIntBuffer() {
        return buffer;
    }

    public int getSize() {
        return getSize(this.type);
    }

} // End of class IBO
