function _fnAddColumn(l,g){var k=DataTable.defaults.columns;
var i=l.aoColumns.length;
var h=$.extend({},DataTable.models.oColumn,k,{sSortingClass:l.oClasses.sSortable,sSortingClassJUI:l.oClasses.sSortJUI,nTh:g?g:document.createElement("th"),sTitle:k.sTitle?k.sTitle:g?g.innerHTML:"",aDataSort:k.aDataSort?k.aDataSort:[i],mData:k.mData?k.oDefaults:i});
l.aoColumns.push(h);
if(l.aoPreSearchCols[i]===undefined||l.aoPreSearchCols[i]===null){l.aoPreSearchCols[i]=$.extend({},DataTable.models.oSearch)
}else{var j=l.aoPreSearchCols[i];
if(j.bRegex===undefined){j.bRegex=true
}if(j.bSmart===undefined){j.bSmart=true
}if(j.bCaseInsensitive===undefined){j.bCaseInsensitive=true
}}_fnColumnOptions(l,i,null)
}function _fnColumnOptions(l,i,j){var g=l.aoColumns[i];
if(j!==undefined&&j!==null){if(j.mDataProp&&!j.mData){j.mData=j.mDataProp
}if(j.sType!==undefined){g.sType=j.sType;
g._bAutoType=false
}$.extend(g,j);
_fnMap(g,j,"sWidth","sWidthOrig");
if(j.iDataSort!==undefined){g.aDataSort=[j.iDataSort]
}_fnMap(g,j,"aDataSort")
}var h=g.mRender?_fnGetObjectDataFn(g.mRender):null;
var k=_fnGetObjectDataFn(g.mData);
g.fnGetData=function(b,a){var c=k(b,a);
if(g.mRender&&(a&&a!=="")){return h(c,a,b)
}return c
};
g.fnSetData=_fnSetObjectDataFn(g.mData);
if(!l.oFeatures.bSort){g.bSortable=false
}if(!g.bSortable||($.inArray("asc",g.asSorting)==-1&&$.inArray("desc",g.asSorting)==-1)){g.sSortingClass=l.oClasses.sSortableNone;
g.sSortingClassJUI=""
}else{if($.inArray("asc",g.asSorting)==-1&&$.inArray("desc",g.asSorting)==-1){g.sSortingClass=l.oClasses.sSortable;
g.sSortingClassJUI=l.oClasses.sSortJUI
}else{if($.inArray("asc",g.asSorting)!=-1&&$.inArray("desc",g.asSorting)==-1){g.sSortingClass=l.oClasses.sSortableAsc;
g.sSortingClassJUI=l.oClasses.sSortJUIAscAllowed
}else{if($.inArray("asc",g.asSorting)==-1&&$.inArray("desc",g.asSorting)!=-1){g.sSortingClass=l.oClasses.sSortableDesc;
g.sSortingClassJUI=l.oClasses.sSortJUIDescAllowed
}}}}}function _fnAdjustColumnSizing(e){if(e.oFeatures.bAutoWidth===false){return false
}_fnCalculateColumnWidths(e);
for(var f=0,d=e.aoColumns.length;
f<d;
f++){e.aoColumns[f].nTh.style.width=e.aoColumns[f].sWidth
}}function _fnVisibleToColumnIndex(e,d){var f=_fnGetColumns(e,"bVisible");
return typeof f[d]==="number"?f[d]:null
}function _fnColumnIndexToVisible(f,h){var g=_fnGetColumns(f,"bVisible");
var e=$.inArray(h,g);
return e!==-1?e:null
}function _fnVisbleColumns(b){return _fnGetColumns(b,"bVisible").length
}function _fnGetColumns(e,f){var a=[];
$.map(e.aoColumns,function(c,b){if(c[f]){a.push(b)
}});
return a
}function _fnDetectType(h){var g=DataTable.ext.aTypes;
var i=g.length;
for(var f=0;
f<i;
f++){var j=g[f](h);
if(j!==null){return j
}}return"string"
}function _fnReOrderIndex(n,k){var h=k.split(",");
var m=[];
for(var i=0,l=n.aoColumns.length;
i<l;
i++){for(var j=0;
j<l;
j++){if(n.aoColumns[i].sName==h[j]){m.push(j);
break
}}}return m
}function _fnColumnOrdering(e){var g="";
for(var f=0,h=e.aoColumns.length;
f<h;
f++){g+=e.aoColumns[f].sName+","
}if(g.length==h){return""
}return g.slice(0,-1)
}function _fnApplyColumnDefs(o,q,k,r){var u,j,v,p,i,s;
if(q){for(u=q.length-1;
u>=0;
u--){var t=q[u].aTargets;
if(!$.isArray(t)){_fnLog(o,1,"aTargets must be an array of targets, not a "+(typeof t))
}for(v=0,p=t.length;
v<p;
v++){if(typeof t[v]==="number"&&t[v]>=0){while(o.aoColumns.length<=t[v]){_fnAddColumn(o)
}r(t[v],q[u])
}else{if(typeof t[v]==="number"&&t[v]<0){r(o.aoColumns.length+t[v],q[u])
}else{if(typeof t[v]==="string"){for(i=0,s=o.aoColumns.length;
i<s;
i++){if(t[v]=="_all"||$(o.aoColumns[i].nTh).hasClass(t[v])){r(i,q[u])
}}}}}}}}if(k){for(u=0,j=k.length;
u<j;
u++){r(u,k[u])
}}};