function _fnAjaxUpdate(e){if(e.bAjaxDataGet){e.iDraw++;
_fnProcessingDisplay(e,true);
var f=e.aoColumns.length;
var d=_fnAjaxParameters(e);
_fnServerParams(e,d);
e.fnServerData.call(e.oInstance,e.sAjaxSource,d,function(a){_fnAjaxUpdateDraw(e,a)
},e);
return false
}else{return true
}}function _fnAjaxParameters(p){var r=p.aoColumns.length;
var i=[],o,q,n;
var l,m;
i.push({name:"sEcho",value:p.iDraw});
i.push({name:"iColumns",value:r});
i.push({name:"sColumns",value:_fnColumnOrdering(p)});
i.push({name:"iDisplayStart",value:p._iDisplayStart});
i.push({name:"iDisplayLength",value:p.oFeatures.bPaginate!==false?p._iDisplayLength:-1});
for(l=0;
l<r;
l++){o=p.aoColumns[l].mData;
i.push({name:"mDataProp_"+l,value:typeof(o)==="function"?"function":o})
}if(p.oFeatures.bFilter!==false){i.push({name:"sSearch",value:p.oPreviousSearch.sSearch});
i.push({name:"bRegex",value:p.oPreviousSearch.bRegex});
for(l=0;
l<r;
l++){i.push({name:"sSearch_"+l,value:p.aoPreSearchCols[l].sSearch});
i.push({name:"bRegex_"+l,value:p.aoPreSearchCols[l].bRegex});
i.push({name:"bSearchable_"+l,value:p.aoColumns[l].bSearchable})
}}if(p.oFeatures.bSort!==false){var j=0;
q=(p.aaSortingFixed!==null)?p.aaSortingFixed.concat(p.aaSorting):p.aaSorting.slice();
for(l=0;
l<q.length;
l++){n=p.aoColumns[q[l][0]].aDataSort;
for(m=0;
m<n.length;
m++){i.push({name:"iSortCol_"+j,value:n[m]});
i.push({name:"sSortDir_"+j,value:q[l][1]});
j++
}}i.push({name:"iSortingCols",value:j});
for(l=0;
l<r;
l++){i.push({name:"bSortable_"+l,value:p.aoColumns[l].bSortable})
}}return i
}function _fnServerParams(c,d){_fnCallbackFire(c,"aoServerParams","serverParams",[d])
}function _fnAjaxUpdateDraw(n,p){if(p.sEcho!==undefined){if(p.sEcho*1<n.iDraw){return
}else{n.iDraw=p.sEcho*1
}}if(!n.oScroll.bInfinite||(n.oScroll.bInfinite&&(n.bSorted||n.bFiltered))){_fnClearTable(n)
}n._iRecordsTotal=parseInt(p.iTotalRecords,10);
n._iRecordsDisplay=parseInt(p.iTotalDisplayRecords,10);
var s=_fnColumnOrdering(n);
var r=(p.sColumns!==undefined&&s!==""&&p.sColumns!=s);
var j;
if(r){j=_fnReOrderIndex(n,p.sColumns)
}var v=_fnGetObjectDataFn(n.sAjaxDataProp)(p);
for(var t=0,i=v.length;
t<i;
t++){if(r){var o=[];
for(var u=0,q=n.aoColumns.length;
u<q;
u++){o.push(v[t][j[u]])
}_fnAddData(n,o)
}else{_fnAddData(n,v[t])
}}n.aiDisplay=n.aiDisplayMaster.slice();
n.bAjaxDataGet=false;
_fnDraw(n);
n.bAjaxDataGet=true;
_fnProcessingDisplay(n,false)
};