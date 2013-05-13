// Vertigo project
// Set geometry and material
var vertices = [
//origin
0,0,0,
//first  square
0,1.25,0.5,
-0.25,1,0.5,
0,0.75,0.5,
0.25,1,0.5,
//second square
-1,0.25,0.5,
-0.75,0,0.5,
-1,-0.25,0.5,
-1.25,0,0.5,
//third square
0,-1.25,0.5,
-0.25,-1,0.5,
0,-0.75,0.5,
0.25,-1,0.5,
//fourd square
1,0.25,0.5,
0.75,0,0.5,
1,-0.25,0.5,
1.25,0,0.5
];

var indices=[0,2,3,0,4,3,0,1,4,1,2,0,6,5,0,7,6,0,8,7,0,5,8,0,11,12,9,12,0,9,10,0,11,10,0,16,15,0,15,14,0,14,13,0,16,13,0];
var viewer = newInstance("Vertigo_Viewer");
viewer.setTitle("Strange thing");
viewer.setBackgroundColor(200,0,0);
viewer.setDimension(900,900);
var scene = viewer.getScene();
// Use of default material
var shape = scene.addNewNode("Shape");
shape.setName("Test");
shape.setGeometry("V3F",vertices);
shape.setIndices(indices);
shape.setDrawingStyle("TRIANGLE_FAN");
shape.setColor(0.4,1.0,1.0);
var cam=viewer.getCamera();
cam.setPosition(0,0,8);
viewer.show();

function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
