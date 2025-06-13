<template>
  <div>
    <div class="card">你好，欢迎使用本系统</div>
    <div class="card" style="margin-top: 10px;width: 50%" v-if="data.user.role==='USER'">
      <div style="font-size: 18px;margin-bottom: 20px">系统公告</div>
      <el-timeline style="max-width: 600px">
        <el-timeline-item :timestamp="item.time" color="#0bbd87" placement="top" v-for="item in data.noticeData">
          <el-card>
            <h4>{{ item.title }}</h4>
            <p>{{ item.content }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
    <div v-else style="margin-top: 10px;">
      <div style="display: flex;grid-gap: 10px">
        <div class="card" style="height: 400px;width: 50%" id="pie"></div>
        <div class="card" style="height: 400px;width: 50%" id="bar"></div>
      </div>
      <div>
        <div class="card" style="height: 400px;width: 100%;margin-top: 10px;" id="line"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted} from "vue";
import request from "@/util/request.js";
import {ElMessage} from "element-plus";
import * as echarts from 'echarts';

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  noticeData: []
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
      if (data.noticeData.length > 3) {
        data.noticeData = data.noticeData.slice(0, 3)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()

const loadPie = () => {
  request.get('/echarts/pie').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('pie')
      let myChart = echarts.init(chartDom)
      pieOptions.series[0].data = res.data
      myChart.setOption(pieOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadBar = () => {
  request.get('/echarts/bar').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('bar')
      let myChart = echarts.init(chartDom)
      barOptions.xAxis.data = res.data.xAxis
      barOptions.series[0].data = res.data.yAxis
      myChart.setOption(barOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadLine = () => {
  request.get('/echarts/line').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('line')
      let myChart = echarts.init(chartDom)
      lineOptions.xAxis.data = res.data.xAxis
      lineOptions.series[0].data = res.data.yAxis
      myChart.setOption(lineOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() => {
  loadPie()
  loadBar()
  loadLine()
})

//饼图
let pieOptions = {
  title: {
    text: '不同分类下用户发布旅游攻略帖子的数量', //主标题
    subtext: '统计维度：攻略分类', //副标题
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量占比', //鼠标移上去显示内容
      type: 'pie',
      radius: '50%',
      center: ['50%', '60%'],
      data: [
        {value: 1048, name: 'Search Engine'},
        {value: 735, name: 'Direct'},
        {value: 580, name: 'Email'}
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}

//柱状图
let barOptions = {
  title: {
    text: '不同用户发布帖子数量Top5', //主标题
    subtext: '统计维度：用户昵称', //副标题
    left: 'center'
  },
  grid: { //距离底部的距离
    top: '20%',
    bottom: '20%'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: ['Total', 'Rent', 'Utilities', 'Transportation', 'Meals', 'Other'],
    name: '用户昵称',
    axisLabel: {
      show: true, //是否显示刻度标签，默认显示
      interval: 0, //坐标轴刻度标签的显示间隔
      rotate: -60, //刻度标签旋转的角度
      inside: false, //刻度标签是否朝内，默认朝外
      margin: 6, //刻度标签与轴线的距离
    },
  },
  yAxis: {
    type: 'value',
    name: '攻略数量'
  },
  tooltip: {
    trigger: 'item',
  },
  series: [
    {
      data: [0, 1700, 1400, 1200, 300, 0],
      type: 'bar',
      itemStyle: {
        normal: {
          color: function () {
            return '#' + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16);
          }
        },
      },
    }
  ]
}

//折线图
let lineOptions = {
  title: {
    text: '近一周旅游攻略发布量折线图', //主标题
    subtext: '统计维度：最近一周', //副标题
    left: 'center'
  },
  legend: {
    data: [],
    template: ""
  },
  grid: { //距离底部的距离
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  tooltip: {
    trigger: 'item',
  },
  xAxis: {
    name: '日期',
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
  },
  yAxis: {
    name: '攻略数量',
    type: 'value'
  },
  series: [
    {
      name: '攻略数量',
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line',
      smooth: true,
      markLine: {
        data: [{type: 'average', name: '最近一周攻略发布数量平均值'}]
      },
      markPoint: {
        data: [
          {type: 'max', name: '最大值'},
          {type: 'min', name: '最小值'}
        ]
      },
    },
  ]
}

</script>
