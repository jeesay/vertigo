package vertigo.graphics;

import java.nio.FloatBuffer;

public class VBO extends BO {
  
  private FloatBuffer buffer;
    private String type;
  
  public VBO() {
    super();
  }
  
  public void setFloatBuffer(String type, FloatBuffer buf) {
        this.type = type;
        buffer = buf;
    }


    public FloatBuffer getFloatBuffer() {
        return buffer;
    }
} // End of class VBO
