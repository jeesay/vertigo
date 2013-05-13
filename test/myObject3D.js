// Set geometry and material
var vertices = [
  // Front face
-0.40,0.0,0.5,
-0.15, 0.25,0.5,
0.15,0.25,0.5,
0.4,0.0,0.5,
0.15,-0.25,0.5,
-0.15, -0.25,0.5,
  // Back face
-0.40,0.0,-0.5,
-0.15,0.25,-0.5,
0.15,0.25,-0.5,
0.4,0.0,-0.5,
0.15,-0.25,-0.5,
-0.15,-0.25,-0.5
];
var indices=[0,6,7,1,0,5,11,6,11,10,4,5,4,10,9,3,4,3,2,1,2,8,9,8,7,1];
// Creation and init viewer
var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("My Test");
viewer.setBackgroundColor(0,0,102);
viewer.setDimension(900,900);
var scene = viewer.getScene();
// Use of default material
var shape = scene.addNewNode("Shape");
shape.setName("Test");
shape.setGeometry("V3F",vertices);
shape.setIndices(indices);
shape.setDrawingStyle("LINE_LOOP");
//set the color of the object
shape.setColor(0.2,0.8,0.0);
var cam=viewer.getCamera();
cam.setPosition(0,0,8);
// Render
viewer.show();

function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
