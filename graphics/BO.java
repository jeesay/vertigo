package vertigo.graphics;


 public class BO {
   
   private int handle;
   
   public BO() {
     handle = -1;
   }
   
   public void setHandle(int value) {
        handle = value;
    }


    public int getHandle() {
        return handle;
    }
    
    public boolean isDirty() {
     return (handle==-1);
    }

} // End of class BO