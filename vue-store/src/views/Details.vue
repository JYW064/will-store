<!--
 * @Description: 商品详情页面组件
 * @Author: hai-27
 * @Date: 2020-02-16 20:20:26
 * @LastEditors: hai-27
 * @LastEditTime: 2020-03-07 21:59:26
 -->
<template>
  <div id="details">
    <!-- 头部 -->
    <div class="page-header">
      <div class="title">
        <p>{{productDetails.productName}}</p>
        <div class="list">
          <ul>
            <li>
              <router-link to>概述</router-link>
            </li>
            <li>
              <router-link to>参数</router-link>
            </li>
            <li>
              <router-link to>用户评价</router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!-- 头部END -->

    <!-- 主要内容 -->
    <div class="main">
      <!-- 左侧商品轮播图 -->
      <div class="block">
        <el-carousel height="560px" v-if="productPicture.length>1">
          <el-carousel-item v-for="item in productPicture" :key="item.id">
            <img style="height:560px;" :src="$target + item.img" :alt="item.info" />
          </el-carousel-item>
        </el-carousel>
        <div v-if="productPicture.length==1">
          <img
            style="height:560px;"
            :src="$target + productPicture[0].img"
            :alt="productPicture[0].info"
          />
        </div>
      </div>
      <!-- 左侧商品轮播图END -->

      <!-- 右侧内容区 -->
      <div class="content">
        <h1 class="name">{{productDetails.productName}}</h1>
        <p class="intro">{{productDetails.info}}</p>
        <p class="store">小米自营</p>
        <div class="price">
          <span>{{productDetails.sellingPrice}}元</span>
          <span
            v-show="productDetails.price != productDetails.sellingPrice"
            class="del"
          >{{productDetails.price}}元</span>
        </div>
        <div class="pro-list">
          <span class="pro-name">{{productDetails.productName}}</span>
          <span class="pro-price">
            <span>{{productDetails.sellingPrice}}元</span>
            <span
              v-show="productDetails.price != productDetails.sellingPrice"
              class="pro-del"
            >{{productDetails.price}}元</span>
          </span>
          <p class="price-sum">总计 : {{productDetails.sellingPrice}}元</p>
        </div>
        <!-- 内容区底部按钮 -->
        <div class="button">
          <el-button class="shop-cart" :disabled="dis" @click="addShoppingCart">加入购物车</el-button>
          <el-button class="like" @click="addCollect">
            <span class="btn-name">{{test}}</span>
          </el-button>
        </div>
        <!-- 内容区底部按钮END -->
        <div class="pro-policy">
          <ul>
            <li>
              <i class="el-icon-circle-check"></i> 小米自营
            </li>
            <li>
              <i class="el-icon-circle-check"></i> 小米发货
            </li>
            <li>
              <i class="el-icon-circle-check"></i> 7天无理由退货
            </li>
            <li>
              <i class="el-icon-circle-check"></i> 7天价格保护
            </li>
          </ul>
        </div>
      </div>
      <!-- 右侧内容区END -->
    </div>
    <!-- 主要内容END -->
  </div>
</template>
<script>
import {mapActions} from "vuex";

export default {
  data() {
    return {
      dis: false, // 控制“加入购物车按钮是否可用”
      productId: "", // 商品id
      productDetails: "", // 商品详细信息
      productPicture: "", // 商品图片
      test: "收藏"
    };
  },
  // 通过路由获取商品id
  activated() {
    if (this.$route.query.productId != undefined) {
      this.productId = this.$route.query.productId;
    }
  },
  watch: {
    // 监听商品id的变化，请求后端获取商品数据
    productId: function(productId) {
      this.getDetails(productId);
      this.getDetailsPicture(productId);
      if (this.$store.getters.getUser)
        this.isCollected(productId);
      else
        this.test = "收藏";
    }
  },
  methods: {
    ...mapActions(["unshiftShoppingCart", "addShoppingCartNum"]),
    // 获取商品详细信息
    getDetails(productId) {
      this.$axios
        .post("http://47.100.75.161:8081/product/getDetails",
            {productId:productId}
        )
        .then(res => {
          this.productDetails = res.data.result[0];
        })
        .catch(err => {
          return Promise.reject(err);
        });
    },
    // 获取商品图片
    getDetailsPicture(productId) {
      this.$axios
        .post("http://47.100.75.161:8081/product/getDetailsPictures",
            {productId}
        )
        .then(res => {
          this.productPicture = res.data.result;
        })
        .catch(err => {
          return Promise.reject(err);
        });
    },
    // 加入购物车
    addShoppingCart() {
      // 判断是否登录,没有登录则显示登录组件
      if (!this.$store.getters.getUser) {
        this.$store.dispatch("setShowLogin", true);
        return;
      }
      this.$axios
        .get(
            "http://47.100.75.161:8081/cart/insertCart"
            //"http://127.0.0.1:8080/cart/insertCart"
            , {
          params:{
            userId: this.$store.getters.getUser.userId,
            productId: this.productId
          }
        })
        .then(res => {
          switch (res.data.code) {
            case "001":
              // 新加入购物车成功
              this.unshiftShoppingCart(res.data.result);
              this.notifySucceed(res.data.msg);
              break;
            case "002":
              // 该商品已经在购物车，数量+1
              this.addShoppingCartNum(this.productId);
              this.notifySucceed(res.data.msg);
              break;
            case "003":
              // 商品数量达到限购数量
              // this.dis = true;
              this.notifyError(res.data.msg);
              break;
            default:
              this.notifyError(res.data.msg);
          }
        })
        .catch(err => {
          return Promise.reject(err);
        });
    },
    addCollect() {
      // 判断是否登录,没有登录则显示登录组件
      if (!this.$store.getters.getUser) {
        this.$store.dispatch("setShowLogin", true);
        return;
      }
      if(this.test=="收藏"){
        this.$axios
            .get("http://47.100.75.161:8081/collect/addCollect", {
              params:{
                userId: this.$store.getters.getUser.userId,
                productId: this.productId
              }
            })
            .then(res => {
              if (res.data.code == "001") {
                // 添加收藏成功
                this.notifySucceed(res.data.msg);
              } else {
                // 添加收藏失败
                this.notifyError(res.data.msg);
              }
            })
            .catch(err => {
              return Promise.reject(err);
            });
        this.test="已收藏";
      }else{
        this.test="收藏";
        this.$axios
            .get("http://47.100.75.161:8081/collect/deleteCollect", {
              params:{
                userId: this.$store.getters.getUser.userId,
                productId: this.productId
              }
            })
            .then(res => {
              if (res.data.code == "001") {
                // 取消收藏成功
                this.notifySucceed(res.data.msg);
              } else {
                // 取消收藏失败
                this.notifyError(res.data.msg);
              }
            })
            .catch(err => {
              return Promise.reject(err);
            });
      }
    },
    isCollected(productId){
      this.$axios
          .get(
              "http://47.100.75.161:8081/collect/isCollected"
              //"http://127.0.0.1:8080/collect/isCollected"
              , {
            params:{
              userId: this.$store.getters.getUser.userId,
              productId: productId
            }
          })
          .then(res => {
            if (res.data.code == "001") {
              this.test = "已收藏"
            } else {
              // 取消收藏失败
              this.test = "收藏"
            }
          })
          .catch(err => {
            return Promise.reject(err);
          });
    }
  }
};
</script>
<style>
/* 头部CSS */
#details .page-header {
  height: 64px;
  margin-top: -20px;
  z-index: 4;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
  -webkit-box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.07);
  box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.07);
}
#details .page-header .title {
  width: 1225px;
  height: 64px;
  line-height: 64px;
  font-size: 18px;
  font-weight: 400;
  color: #212121;
  margin: 0 auto;
}
#details .page-header .title p {
  float: left;
}
#details .page-header .title .list {
  height: 64px;
  float: right;
}
#details .page-header .title .list li {
  float: left;
  margin-left: 20px;
}
#details .page-header .title .list li a {
  font-size: 14px;
  color: #616161;
}
#details .page-header .title .list li a:hover {
  font-size: 14px;
  color: #ff6700;
}
/* 头部CSS END */

/* 主要内容CSS */
#details .main {
  width: 1225px;
  height: 560px;
  padding-top: 30px;
  margin: 0 auto;
}
#details .main .block {
  float: left;
  width: 560px;
  height: 560px;
}
#details .el-carousel .el-carousel__indicator .el-carousel__button {
  background-color: rgba(163, 163, 163, 0.8);
}
#details .main .content {
  float: left;
  margin-left: 25px;
  width: 640px;
}
#details .main .content .name {
  height: 30px;
  line-height: 30px;
  font-size: 24px;
  font-weight: normal;
  color: #212121;
}
#details .main .content .intro {
  color: #b0b0b0;
  padding-top: 10px;
}
#details .main .content .store {
  color: #ff6700;
  padding-top: 10px;
}
#details .main .content .price {
  display: block;
  font-size: 18px;
  color: #ff6700;
  border-bottom: 1px solid #e0e0e0;
  padding: 25px 0 25px;
}
#details .main .content .price .del {
  font-size: 14px;
  margin-left: 10px;
  color: #b0b0b0;
  text-decoration: line-through;
}
#details .main .content .pro-list {
  background: #f9f9fa;
  padding: 30px 60px;
  margin: 50px 0 50px;
}
#details .main .content .pro-list span {
  line-height: 30px;
  color: #616161;
}
#details .main .content .pro-list .pro-price {
  float: right;
}
#details .main .content .pro-list .pro-price .pro-del {
  margin-left: 10px;
  text-decoration: line-through;
}
#details .main .content .pro-list .price-sum {
  color: #ff6700;
  font-size: 24px;
  padding-top: 20px;
}
#details .main .content .button {
  height: 55px;
  margin: 10px 0 20px 0;
}
#details .main .content .button .el-button {
  float: left;
  height: 55px;
  font-size: 16px;
  color: #fff;
  border: none;
  text-align: center;
}
#details .main .content .button .shop-cart {
  width: 340px;
  background-color: #ff6700;
}
#details .main .content .button .shop-cart:hover {
  background-color: #f25807;
}

#details .main .content .button .like {
  width: 260px;
  margin-left: 40px;
  background-color: #b0b0b0;
}
#details .main .content .button .like:hover {
  background-color: #757575;
}
#details .main .content .pro-policy li {
  float: left;
  margin-right: 20px;
  color: #b0b0b0;
}
/* 主要内容CSS END */
</style>
