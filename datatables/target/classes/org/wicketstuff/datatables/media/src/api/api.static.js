DataTable.fnVersionCheck=function(p){var i=function(b,a){while(b.length<a){b+="0"
}return b
};
var j=DataTable.ext.sVersion.split(".");
var m=p.split(".");
var k="",n="";
for(var l=0,o=m.length;
l<o;
l++){k+=i(j[l],3);
n+=i(m[l],3)
}return parseInt(k,10)>=parseInt(n,10)
};
DataTable.fnIsDataTable=function(d){var e=DataTable.settings;
for(var f=0;
f<e.length;
f++){if(e[f].nTable===d||e[f].nScrollHead===d||e[f].nScrollFoot===d){return true
}}return false
};
DataTable.fnTables=function(d){var c=[];
jQuery.each(DataTable.settings,function(b,a){if(!d||(d===true&&$(a.nTable).is(":visible"))){c.push(a.nTable)
}});
return c
};