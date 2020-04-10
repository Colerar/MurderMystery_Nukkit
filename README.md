# MurderMystery  
#### 本插件仍在开发中！  
密室杀人游戏  
#### 已实现功能：  
- [X] 多房间  
- [X] 游戏核心玩法  
  - [X] 侦探死后掉弓  
  - [X] 被杀生成尸体  
- [X] 进退游戏保存背包  
#### 需要添加的内容：   
- [ ] 给玩家设置随机皮肤  
  
#### 对于开发者：
API请参考：  
main/java/name/murdermystery/api/Api.java  
  
插件提供事件： 
 - MurderPlayerDamageEvent 玩家被攻击事件  
   如：杀手用剑打人，平民或侦探用弓打人
 - MurderPlayerDeathEvent 玩家死亡事件  
   如：杀手被平民或侦探击杀，平民或侦探被杀手击杀 