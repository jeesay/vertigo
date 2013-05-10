var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Cube");
viewer.setBackgroundColor(225,200,0);

// Get Camera and translate it 
var cam = viewer.getCamera();
cam.setPosition(0,0,8);

// Get Scene 
var scene = viewer.getScene();
var cube=scene.addNewNode("WireCube");
var mat=cube.getMaterial();
mat.setColor(0.0,0.0,1.0);

viewer.show("LWJGL");

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
