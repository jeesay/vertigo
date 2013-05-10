var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Cube");
viewer.setBackgroundColor(50,200,0);
// Get Scene 
var scene = viewer.getScene();
scene.addNewNode("WireCube");

viewer.show("JOGL");

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
