(ns baoqu.components.home)

(:require [rum.core :as rum])

(rum/defc main < rum/reactive
  "The main component for the home screen"
  []
  (header))

(rum/defc header < rum/reactive
  []
  [:div#mainHeader
   [:h1.logo "Baoqu"]
   [:div.event-name "¿Qué tipo de muerte dolorosa queremos para los ciclistas de La Guindalera?"]])







;;         <!-- CONTAINER-->
;;         <div class="container">
;;           <!-- MAPA-->
;;           <div class="map">mapa</div>
;;           <!--CIRCLE-->
;;           <div class="circle-wrapper">
;;             <div class="circle-header">
;;               <div class="circle-header-title">Círculo Onisuzume</div><span class="circle-header-exit">Salir de este círculo</span>
;;             </div>
;;             <div class="circle-content">
;;               <!-- IDEAS-->
;;               <div class="mod-ideas">
;;                 <div class="mod-header">
;;                   <div class="title">36 ideas</div><span class="toggle"><i class="fa fa-lg fa-chevron-right"></i></span>
;;                 </div>
;;                 <div class="mod-body">ideas body</div>
;;                 <div class="mod-add-box">
;;                   <input placeholder="Añade una nueva idea"/><span class="button"><i class="fa fa-lg fa-plus"></i></span>
;;                 </div>
;;               </div>
;;               <!-- COMMENTS-->
;;               <div class="mod-comments">
;;                 <div class="mod-header">
;;                   <div class="title">36 comments</div><span class="toggle"><i class="fa fa-lg fa-chevron-right"></i></span>
;;                 </div>
;;                 <div class="mod-body">comments body</div>
;;                 <div class="mod-add-box">
;;                   <input placeholder="Comenta"/><span class="button"><i class="fa fa-lg fa-plus"></i></span>
;;                 </div>
;;               </div>
;;             </div>
;;           </div>
;;         </div>
;;         <!-- FOOTER-->
;;         <div id="mainFooter">user movidas</div>
;;       </div>
;;     </body>
;;   </head>
;; </html>
