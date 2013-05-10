// Test1: Display vertices of a cube 
// Vertigo project

// Set geometry and material
var vertices = [
  // Front face
  -0.40,0.0,  0.5,
   -0.15, 0.25,  0.5,
   0.15,  0.25,  0.5,
0.4,  0.0,  0.5,
0.15,  -0.25,  0.5,
  -0.15,  -0.25,  0.5,
   
  // Back face
   -0.40,0.0,  -0.5,
   -0.15, 0.25,  -0.5,
   0.15,  0.25,  -0.5,
0.4,  0.0, -0.5,
0.15,  -0.25,  -0.5,
  -0.15,  -0.25,  -0.5
];

var color = [
  // Front face
1.0, 0.0, 0.0,
1.0, 0.0, 0.0,
1.0, 0.0, 0.0,
1.0, 0.0, 0.0,
1.0, 0.0, 0.0,
1.0, 0.0, 0.0,
  // Back face
 1.0, 1.0, 0.0,
 1.0, 1.0, 0.0,
 1.0, 1.0, 0.0,
 1.0, 1.0, 0.0,
 1.0, 1.0, 0.0,
 1.0, 1.0, 0.0
];

var indices=[0,6,7,1,0,5,11,6,11,10,4,5,4,10,9,3,4,3,2,1,2,8,9,8,7,1];

// Creation and init viewer
var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("My Test");
viewer.setBackgroundColor(0,250,0);
viewer.setDimension(900,900);

// Get Camera and translate it 
var cam = viewer.getCamera();
cam.setPosition(0.0,0.0,4.0);

// Get Scene 
var scene = viewer.getScene();
IJ.log(scene);



//Create and add a shape entitled "Cube" to the scene
// Use of default material
var cube = scene.addNewNode("Shape");
cube.setName("Cube");
cube.setGeometry("V3F",vertices);
cube.setGeometry("C3F",color);
cube.setIndices(indices);
cube.setDrawingStyle("LINE_LOOP");
// Render
viewer.show();

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
