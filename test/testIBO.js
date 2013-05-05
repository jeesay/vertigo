// Test1: Display vertices of a cube 
// Vertigo project

// Set geometry and material
var vertices = [
  // Front face
  -0.5, -0.5,  0.5,
   0.5, -0.5,  0.5,
   0.5,  0.5,  0.5,
  -0.5,  0.5,  0.5,
   
  // Back face
  -0.5, -0.5, -0.5,
  -0.5,  0.5, -0.5,
   0.5,  0.5, -0.5,
   0.5, -0.5, -0.5
];

var color = [
  // Front face
  0.0, 0.0,  0.7,
   0.0, 0.0,  0.7,
   0.0,  0.0,  0.7,
  0.0,  0.0,  0.7,
  // Back face
  0.0, 0.0, 0.7,
  0.0,  0.0, 0.7,
   0.0,  0.0, 0.7,
   0.0, 0.0, 0.7
];

var indices=[0, 1, 2, 3, 0, 4, 5, 3, 5, 6, 2, 6, 7, 1, 7, 4];

// Creation and init viewer
var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Test #1");

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
cube.setDrawingStyle("TRIANGLE_FAN");
// Render
viewer.show();

// F U N C T I O N S
// Load and instanciation of a class
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
