# cluster-kmeans
java实现聚类k-means算法

1. K-means算法的原理
   
   K-means算法是最常用的聚类算法，主要思想是:在给定K值和K个初始类簇中心点的情况下，把每个点(亦即数据记录)分到离其最近的类簇中心点所代表的类簇中，所有点分配完毕之后，根据一个类簇内的所有点重新计算该类簇的中心点(取平均值)，然后再迭代的进行分配点和更新类簇中心点的步骤，直至类簇中心点的变化很小，或者达到指定的迭代次数。
   
2. 本项目实现动态配置参数
   
   初始化质心算法
   随机种子
   簇个数
   最大迭代次数
   是否排序簇中实例（按离质心距离远近）
   最小误差算法
   
   
3. 算法动态扩展
   
   初始化质心算法：随机算法....
   最小误差距离算法：欧式距离算法、曼哈顿距离算法...
   
4. 平方和误差
   
   使用SSE算法计算平方和误差用以衡量聚类是否合适。