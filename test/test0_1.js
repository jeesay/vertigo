// Test0_1: Open an OpenGL window
// Vertigo project
// April 2013

// Creation and init viewer
var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Test #0_1");
viewer.setBackgroundColor(255,128,50); // Orange
viewer.setDimension(640,480);
//viewer.default_scenegraph();

//add new nodes
var world=viewer.getWorld();
world.addNewNode("Stage");
world.addNewNode("BackStage");
var node=viewer.getNode("stage");
node.addNewNode("Scene");




// Render's name
viewer.show("TEXT");

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
