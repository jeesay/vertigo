var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Cube");
viewer.setBackgroundColor(50,200,0);

// Get Camera and translate it 
var cam = viewer.getCamera();
cam.setPosition(0.0,0.0,15.0);

// Get Scene 
var scene = viewer.getScene();
var cube1=scene.addNewNode("WireCube");
cube1.translate(-1.5,0,0);
var cube2=scene.addNewNode("WireCube");
cube2.translate(1.5,0,0);

viewer.show("LWJGL");

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
