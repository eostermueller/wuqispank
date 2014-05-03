this.$=function(z,y){var r,s,x=[],a;
var u=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
var t=u.aoData;
var p=u.aiDisplay;
var q=u.aiDisplayMaster;
if(!y){y={}
}y=$.extend({},{filter:"none",order:"current",page:"all"},y);
if(y.page=="current"){for(r=u._iDisplayStart,s=u.fnDisplayEnd();
r<s;
r++){a=t[p[r]].nTr;
if(a){x.push(a)
}}}else{if(y.order=="current"&&y.filter=="none"){for(r=0,s=q.length;
r<s;
r++){a=t[q[r]].nTr;
if(a){x.push(a)
}}}else{if(y.order=="current"&&y.filter=="applied"){for(r=0,s=p.length;
r<s;
r++){a=t[p[r]].nTr;
if(a){x.push(a)
}}}else{if(y.order=="original"&&y.filter=="none"){for(r=0,s=t.length;
r<s;
r++){a=t[r].nTr;
if(a){x.push(a)
}}}else{if(y.order=="original"&&y.filter=="applied"){for(r=0,s=t.length;
r<s;
r++){a=t[r].nTr;
if($.inArray(r,p)!==-1&&a){x.push(a)
}}}else{_fnLog(u,1,"Unknown selection options")
}}}}}var w=$(x);
var v=w.filter(z);
var i=w.find(z);
return $([].concat($.makeArray(v),$.makeArray(i)))
};
this._=function(m,n){var h=[];
var j,l,i;
var k=this.$(m,n);
for(j=0,l=k.length;
j<l;
j++){h.push(this.fnGetData(k[j]))
}return h
};
this.fnAddData=function(h,k){if(h.length===0){return[]
}var l=[];
var g;
var i=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
if(typeof h[0]==="object"&&h[0]!==null){for(var j=0;
j<h.length;
j++){g=_fnAddData(i,h[j]);
if(g==-1){return l
}l.push(g)
}}else{g=_fnAddData(i,h);
if(g==-1){return l
}l.push(g)
}i.aiDisplay=i.aiDisplayMaster.slice();
if(k===undefined||k){_fnReDraw(i)
}return l
};
this.fnAdjustColumnSizing=function(c){var d=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
_fnAdjustColumnSizing(d);
if(c===undefined||c){this.fnDraw(false)
}else{if(d.oScroll.sX!==""||d.oScroll.sY!==""){this.oApi._fnScrollDraw(d)
}}};
this.fnClearTable=function(c){var d=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
_fnClearTable(d);
if(c===undefined||c){_fnDraw(d)
}};
this.fnClose=function(e){var g=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
for(var h=0;
h<g.aoOpenRows.length;
h++){if(g.aoOpenRows[h].nParent==e){var f=g.aoOpenRows[h].nTr.parentNode;
if(f){f.removeChild(g.aoOpenRows[h].nTr)
}g.aoOpenRows.splice(h,1);
return 0
}}return 1
};
this.fnDeleteRow=function(i,k,o){var q=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
var n,p,m;
m=(typeof i==="object")?_fnNodeToDataIndex(q,i):i;
var r=q.aoData.splice(m,1);
for(n=0,p=q.aoData.length;
n<p;
n++){if(q.aoData[n].nTr!==null){q.aoData[n].nTr._DT_RowIndex=n
}}var l=$.inArray(m,q.aiDisplay);
q.asDataSearch.splice(l,1);
_fnDeleteIndex(q.aiDisplayMaster,m);
_fnDeleteIndex(q.aiDisplay,m);
if(typeof k==="function"){k.call(this,q,r)
}if(q._iDisplayStart>=q.fnRecordsDisplay()){q._iDisplayStart-=q._iDisplayLength;
if(q._iDisplayStart<0){q._iDisplayStart=0
}}if(o===undefined||o){_fnCalculateEnd(q);
_fnDraw(q)
}return r
};
this.fnDestroy=function(k){var m=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
var i=m.nTableWrapper.parentNode;
var n=m.nTBody;
var j,l;
k=(k===undefined)?false:k;
m.bDestroying=true;
_fnCallbackFire(m,"aoDestroyCallback","destroy",[m]);
if(!k){for(j=0,l=m.aoColumns.length;
j<l;
j++){if(m.aoColumns[j].bVisible===false){this.fnSetColumnVis(j,true)
}}}$(m.nTableWrapper).find("*").andSelf().unbind(".DT");
$("tbody>tr>td."+m.oClasses.sRowEmpty,m.nTable).parent().remove();
if(m.nTable!=m.nTHead.parentNode){$(m.nTable).children("thead").remove();
m.nTable.appendChild(m.nTHead)
}if(m.nTFoot&&m.nTable!=m.nTFoot.parentNode){$(m.nTable).children("tfoot").remove();
m.nTable.appendChild(m.nTFoot)
}m.nTable.parentNode.removeChild(m.nTable);
$(m.nTableWrapper).remove();
m.aaSorting=[];
m.aaSortingFixed=[];
_fnSortingClasses(m);
$(_fnGetTrNodes(m)).removeClass(m.asStripeClasses.join(" "));
$("th, td",m.nTHead).removeClass([m.oClasses.sSortable,m.oClasses.sSortableAsc,m.oClasses.sSortableDesc,m.oClasses.sSortableNone].join(" "));
if(m.bJUI){$("th span."+m.oClasses.sSortIcon+", td span."+m.oClasses.sSortIcon,m.nTHead).remove();
$("th, td",m.nTHead).each(function(){var a=$("div."+m.oClasses.sSortJUIWrapper,this);
var b=a.contents();
$(this).append(b);
a.remove()
})
}if(!k&&m.nTableReinsertBefore){i.insertBefore(m.nTable,m.nTableReinsertBefore)
}else{if(!k){i.appendChild(m.nTable)
}}for(j=0,l=m.aoData.length;
j<l;
j++){if(m.aoData[j].nTr!==null){n.appendChild(m.aoData[j].nTr)
}}if(m.oFeatures.bAutoWidth===true){m.nTable.style.width=_fnStringToCss(m.sDestroyWidth)
}l=m.asDestroyStripes.length;
if(l){var h=$(n).children("tr");
for(j=0;
j<l;
j++){h.filter(":nth-child("+l+"n + "+j+")").addClass(m.asDestroyStripes[j])
}}for(j=0,l=DataTable.settings.length;
j<l;
j++){if(DataTable.settings[j]==m){DataTable.settings.splice(j,1)
}}m=null;
oInit=null
};
this.fnDraw=function(d){var c=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
if(d===false){_fnCalculateEnd(c);
_fnDraw(c)
}else{_fnReDraw(c)
}};
this.fnFilter=function(p,u,r,q,v,s){var o=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
if(!o.oFeatures.bFilter){return
}if(r===undefined||r===null){r=false
}if(q===undefined||q===null){q=true
}if(v===undefined||v===null){v=true
}if(s===undefined||s===null){s=true
}if(u===undefined||u===null){_fnFilterComplete(o,{sSearch:p+"",bRegex:r,bSmart:q,bCaseInsensitive:s},1);
if(v&&o.aanFeatures.f){var i=o.aanFeatures.f;
for(var e=0,n=i.length;
e<n;
e++){try{if(i[e]._DT_Input!=document.activeElement){$(i[e]._DT_Input).val(p)
}}catch(t){$(i[e]._DT_Input).val(p)
}}}}else{$.extend(o.aoPreSearchCols[u],{sSearch:p+"",bRegex:r,bSmart:q,bCaseInsensitive:s});
_fnFilterComplete(o,o.oPreviousSearch,1)
}};
this.fnGetData=function(f,h){var g=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
if(f!==undefined){var j=f;
if(typeof f==="object"){var i=f.nodeName.toLowerCase();
if(i==="tr"){j=_fnNodeToDataIndex(g,f)
}else{if(i==="td"){j=_fnNodeToDataIndex(g,f.parentNode);
h=_fnNodeToColumnIndex(g,j,f)
}}}if(h!==undefined){return _fnGetCellData(g,j,h,"")
}return(g.aoData[j]!==undefined)?g.aoData[j]._aData:null
}return _fnGetDataMaster(g)
};
this.fnGetNodes=function(c){var d=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
if(c!==undefined){return(d.aoData[c]!==undefined)?d.aoData[c].nTr:null
}return _fnGetTrNodes(d)
};
this.fnGetPosition=function(g){var f=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
var j=g.nodeName.toUpperCase();
if(j=="TR"){return _fnNodeToDataIndex(f,g)
}else{if(j=="TD"||j=="TH"){var h=_fnNodeToDataIndex(f,g.parentNode);
var i=_fnNodeToColumnIndex(f,h,g);
return[h,_fnColumnIndexToVisible(f,i),i]
}}return null
};
this.fnIsOpen=function(e){var f=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
var g=f.aoOpenRows;
for(var h=0;
h<f.aoOpenRows.length;
h++){if(f.aoOpenRows[h].nParent==e){return true
}}return false
};
this.fnOpen=function(i,m,p){var k=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
var n=_fnGetTrNodes(k);
if($.inArray(i,n)===-1){return
}this.fnClose(i);
var o=document.createElement("tr");
var j=document.createElement("td");
o.appendChild(j);
j.className=p;
j.colSpan=_fnVisbleColumns(k);
if(typeof m==="string"){j.innerHTML=m
}else{$(j).html(m)
}var l=$("tr",k.nTBody);
if($.inArray(i,l)!=-1){$(o).insertAfter(i)
}k.aoOpenRows.push({nTr:o,nParent:i});
return o
};
this.fnPageChange=function(d,e){var f=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
_fnPageChange(f,d);
_fnCalculateEnd(f);
if(e===undefined||e){_fnDraw(f)
}};
this.fnSetColumnVis=function(v,w,o){var q=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
var i,p;
var t=q.aoColumns;
var s=q.aoData;
var n,x,u;
if(t[v].bVisible==w){return
}if(w){var r=0;
for(i=0;
i<v;
i++){if(t[i].bVisible){r++
}}x=(r>=_fnVisbleColumns(q));
if(!x){for(i=v;
i<t.length;
i++){if(t[i].bVisible){u=i;
break
}}}for(i=0,p=s.length;
i<p;
i++){if(s[i].nTr!==null){if(x){s[i].nTr.appendChild(s[i]._anHidden[v])
}else{s[i].nTr.insertBefore(s[i]._anHidden[v],_fnGetTdNodes(q,i)[u])
}}}}else{for(i=0,p=s.length;
i<p;
i++){if(s[i].nTr!==null){n=_fnGetTdNodes(q,i)[v];
s[i]._anHidden[v]=n;
n.parentNode.removeChild(n)
}}}t[v].bVisible=w;
_fnDrawHead(q,q.aoHeader);
if(q.nTFoot){_fnDrawHead(q,q.aoFooter)
}for(i=0,p=q.aoOpenRows.length;
i<p;
i++){q.aoOpenRows[i].nTr.colSpan=_fnVisbleColumns(q)
}if(o===undefined||o){_fnAdjustColumnSizing(q);
_fnDraw(q)
}_fnSaveState(q)
};
this.fnSettings=function(){return _fnSettingsFromNode(this[DataTable.ext.iApiIndex])
};
this.fnSort=function(d){var c=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
c.aaSorting=d;
_fnSort(c)
};
this.fnSortListener=function(f,d,e){_fnSortAttachListener(_fnSettingsFromNode(this[DataTable.ext.iApiIndex]),f,d,e)
};
this.fnUpdate=function(n,u,x,p,s){var r=_fnSettingsFromNode(this[DataTable.ext.iApiIndex]);
var i,q,v;
var t=(typeof u==="object")?_fnNodeToDataIndex(r,u):u;
if($.isArray(n)&&x===undefined){r.aoData[t]._aData=n.slice();
for(i=0;
i<r.aoColumns.length;
i++){this.fnUpdate(_fnGetCellData(r,t,i),t,i,false,false)
}}else{if($.isPlainObject(n)&&x===undefined){r.aoData[t]._aData=$.extend(true,{},n);
for(i=0;
i<r.aoColumns.length;
i++){this.fnUpdate(_fnGetCellData(r,t,i),t,i,false,false)
}}else{_fnSetCellData(r,t,x,n);
v=_fnGetCellData(r,t,x,"display");
var o=r.aoColumns[x];
if(o.fnRender!==null){v=_fnRender(r,t,x);
if(o.bUseRendered){_fnSetCellData(r,t,x,v)
}}if(r.aoData[t].nTr!==null){_fnGetTdNodes(r,t)[x].innerHTML=v
}}}var w=$.inArray(t,r.aiDisplay);
r.asDataSearch[w]=_fnBuildSearchRow(r,_fnGetRowData(r,t,"filter",_fnGetColumns(r,"bSearchable")));
if(s===undefined||s){_fnAdjustColumnSizing(r)
}if(p===undefined||p){_fnReDraw(r)
}return 0
};
this.fnVersionCheck=DataTable.ext.fnVersionCheck;