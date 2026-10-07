const r=document.documentElement,b=document.getElementById('tema');
let g=null;try{g=localStorage.getItem('tema')}catch(e){}
if(g)r.dataset.tema=g;
b.addEventListener('click',()=>{const osc=getComputedStyle(r).colorScheme==='dark';const n=osc?'claro':'oscuro';r.dataset.tema=n;try{localStorage.setItem('tema',n)}catch(e){}});
