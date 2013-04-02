// Display 3D vectorial data
// Jean-Christophe Taveau
// Nov 2012
// http://www.crazybiocomputing.blogspot.com


// Set geometry and material
var cube = [
0.0, 0.0, 0.0,
1.0, 0.0, 0.0,
1.0, 1.0, 0.0,
0.0, 1.0, 0.0,
0.0, 0.0, 1.0,
1.0, 0.0, 1.0,
1.0, 1.0, 1.0,
0.0, 1.0, 1.0
];

var indexes = [
0,1,2,3,0, // front face
4,5,6,7,4, // back face
0,1,1,2,2,3,3,4,4,0, // left face
0,1,1,2,2,3,3,4,4,0, // right face
0,1,1,2,2,3,3,4,4,0, // top face
0,1,1,2,2,3,3,4,4,0, // bottom face
];

var viewer = newInstance("Vertigo_jogl");
viewer.setBackgroundColor(100,200,255);
viewer.setTitle("Test");
viewer.setBoundingBox(0,0,0,100,200,300);

IJ.log(viewer);

var scene = viewer.getScene();
var cube = scene.addNewNode("Shape","Cube");

shape.setGeometry(["V3F","N3F","T2F","C3F"],cube);
shape.setGeometry(["V3F"],cube);
shape.setGraphicsType("LINES");
shape.setIndices(indexes);
// Material
shape.setColor(1.0,0.5,0.1);
shape.setStyle("WIREFRAME");
shape.setShaderMaterial("phong");

if (viewer.check("SCENEGRAPH"))
	viewer.show();

// F U N C T I O N S

// Load and instanciation of a class in plugins directory
// Thanks to Wayne Rasband
function newInstance(classname) {
  return IJ.getClassLoader().loadClass(classname).newInstance();
}
