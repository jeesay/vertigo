var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Cube");
viewer.setBackgroundColor(50,200,0);

// Get Camera and translate it 
var cam = viewer.getCamera();
cam.setPosition(0.0,0.0,8.0);

// Get Scene 
var scene = viewer.getScene();
var cube=scene.addNewNode("WireCube");
cube.setColor(100,100,50);

viewer.show("LWJGL");

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
