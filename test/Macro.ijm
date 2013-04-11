names = newArray(
"BackStage",               // 0: 
            "Camera",               // 0: 
            "Cube",               // 0: 
            "FlatCube",               // 0: 
            "FlatPyramid",               // 0: 
            "FlatTetrahedron",               // 0: 
            "Group",                // 0: 
            "Light",                // 0: 
            "Lighting",                // 0: 
            "Pyramid",
            "Scene",               // 0: 
            "Shape",               // 0: 
            "Sphere",               // 0: 
            "Stage",               // 0: 
            "Tetrahedron",               // 0: 
            "Torus",               // 0: 
            "Transform",               // 0: 
            "Viewing",               // 0: 
            "WireCube",               // 0: 
            "WirePyramid",               // 0: 
            "WireSphere",               // 0: 
            "WireTorus"
);

for (i=0;i<names.length;i++) {
  sum = 0;
  for (j=0;j<lengthOf(names[i]);j++) {
    str = names[i];
    sum +=charCodeAt(str,j);
  }
  print("case "+sum+": // "+names[i]);

}
print (" default: // Do nothing  ");
