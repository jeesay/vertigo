var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Cube");
viewer.setBackgroundColor(0,0,0);
// Get Scene 
var scene = viewer.getScene();
scene.addNewNode("Pyramid");

viewer.show("LWJGL");

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
