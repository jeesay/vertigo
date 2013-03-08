
public class VBO extends BO {
  
  private FloatBuffer buffer;
  
  public VBO() {
    super();
  }
  
  public void setFloatBuffer(String type, FloatBuffer buf) {
        this.type = type;
        buffer = buf;
    }

    public void setFloatBuffer(String[] types, FloatBuffer buf) {
        this.types = types;
        packed = true;
        buffer = buf;
        this.stride = calc_stride(types);
    }

    public FloatBuffer getFloatBuffer() {
        return buffer;
    }
} // End of class VBO
