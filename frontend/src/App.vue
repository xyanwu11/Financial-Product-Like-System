<template>
  <div class="app">
    <div v-if="!currentUser" class="login-page">
      <div class="login-card">
        <h1 class="login-title">金融商品喜好紀錄系統</h1>
        <p class="login-subtitle">Financial Product Like System</p>

        <div v-if="!showRegister">
          <div class="modal-body">
            <div class="form-group">
              <label class="form-label">使用者 ID</label>
              <input class="form-input" v-model="loginForm.userId" placeholder="請輸入使用者 ID" @keyup.enter="handleLogin"/>
            </div>
            <div class="form-group">
              <label class="form-label">密碼</label>
              <input class="form-input" type="password" v-model="loginForm.password" placeholder="請輸入密碼" @keyup.enter="handleLogin"/>
            </div>
          </div>
          <button class="btn btn-primary" style="width:100%;margin-top:20px" @click="handleLogin" :disabled="submitting">登入</button>
          <p class="login-switch">還沒有帳號？<a @click="showRegister = true">註冊</a></p>
        </div>

        <div v-else>
          <div class="modal-body">
            <div class="form-group">
              <label class="form-label">使用者 ID</label>
              <input class="form-input" v-model="regForm.userId" placeholder="最多 20 字"/>
            </div>
            <div class="form-group">
              <label class="form-label">姓名</label>
              <input class="form-input" v-model="regForm.userName" placeholder="您的姓名"/>
            </div>
            <div class="form-group">
              <label class="form-label">Email</label>
              <input class="form-input" type="email" v-model="regForm.email" placeholder="email@example.com"/>
            </div>
            <div class="form-group">
              <label class="form-label">扣款帳號</label>
              <input class="form-input" v-model="regForm.account" placeholder="銀行帳號"/>
            </div>
            <div class="form-group">
              <label class="form-label">密碼</label>
              <input class="form-input" type="password" v-model="regForm.password" placeholder="至少 4 字" @keyup.enter="handleRegister"/>
            </div>
          </div>
          <button class="btn btn-primary" style="width:100%;margin-top:20px" @click="handleRegister" :disabled="submitting">註冊</button>
          <p class="login-switch">已有帳號？<a @click="showRegister = false">登入</a></p>
        </div>
      </div>
    </div>

    <template v-else>
      <header class="app-header">
        <div class="header-inner">
          <h1 class="logo">金融商品喜好紀錄系統</h1>
          <div class="header-right">
            <span class="user-badge" :class="isAdmin ? 'badge-admin' : 'badge-user'">
              {{ isAdmin ? 'ADMIN' : 'USER' }}
            </span>
            <span class="user-name">{{ currentUser.userName }}</span>
            <button class="btn btn-outline btn-sm" @click="logout">登出</button>
          </div>
        </div>
      </header>

      <main class="main-content">
        <div class="toolbar">
          <span class="text-muted">共 {{ likeList.length }} 筆喜好商品</span>
          <button class="btn btn-primary" @click="openAddModal">＋ 新增喜好商品</button>
        </div>

        <div v-if="likeList.length > 0" class="table-container">
          <table>
            <thead>
              <tr>
                <th>序號</th>
                <th v-if="isAdmin">使用者</th>
                <th>產品名稱</th>
                <th>產品價格</th>
                <th>手續費率</th>
                <th>購買數量</th>
                <th>扣款帳號</th>
                <th>總手續費</th>
                <th>預計扣款總金額</th>
                <th>電子信箱</th>
                <th class="text-center">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in likeList" :key="item.sn">
                <td>{{ item.sn }}</td>
                <td v-if="isAdmin"><strong>{{ item.userName }}</strong></td>
                <td><strong>{{ item.productName }}</strong></td>
                <td class="amount">NT$ {{ fmt(item.price) }}</td>
                <td>{{ (item.feeRate * 100).toFixed(2) }}%</td>
                <td>{{ item.purchaseQuantity }}</td>
                <td><code>{{ item.account }}</code></td>
                <td class="amount">NT$ {{ fmt(item.totalFee) }}</td>
                <td class="amount" style="color:var(--primary-hover)">NT$ {{ fmt(item.totalAmount) }}</td>
                <td class="text-muted">{{ item.email }}</td>
                <td class="text-center">
                  <div style="display:flex;gap:6px;justify-content:center">
                    <button class="btn btn-outline btn-sm" @click="openEditModal(item)">編輯</button>
                    <button class="btn btn-danger btn-sm" @click="confirmDelete(item.sn)">刪除</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="likeList.length === 0 && !loading" class="empty-state card">
          <p>尚無喜好商品，點擊上方按鈕新增</p>
        </div>
        <div v-if="loading" class="empty-state"><div class="spinner"></div></div>
      </main>

      <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
        <div class="modal">
          <div class="modal-header">
            <h2 class="modal-title">{{ isEdit ? '編輯喜好商品' : '新增喜好商品' }}</h2>
            <button class="btn btn-icon btn-outline" @click="closeModal">&times;</button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label class="form-label">金融商品</label>
              <select class="form-select" v-model="form.productNo" @change="onProductChange">
                <option value="">請選擇商品</option>
                <option v-for="p in products" :key="p.no" :value="p.no">
                  {{ p.productName }} - NT$ {{ fmt(p.price) }}
                </option>
              </select>
            </div>
            <div v-if="isEdit" style="display:grid;grid-template-columns:1fr 1fr 1fr;gap:12px">
              <div class="form-group">
                <label class="form-label">產品名稱</label>
                <input class="form-input" v-model="form.productName"/>
              </div>
              <div class="form-group">
                <label class="form-label">產品價格</label>
                <input class="form-input" type="number" step="0.01" v-model.number="form.price"/>
              </div>
              <div class="form-group">
                <label class="form-label">手續費率</label>
                <input class="form-input" type="number" step="0.0001" v-model.number="form.feeRate"/>
              </div>
            </div>
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px">
              <div class="form-group">
                <label class="form-label">購買數量</label>
                <input class="form-input" type="number" min="1" v-model.number="form.purchaseQuantity"/>
              </div>
              <div class="form-group">
                <label class="form-label">扣款帳號</label>
                <input class="form-input" v-model="form.account" :disabled="!isAdmin"/>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-outline" @click="closeModal">取消</button>
            <button class="btn btn-primary" @click="submitForm" :disabled="submitting">
              {{ isEdit ? '更新' : '新增' }}
            </button>
          </div>
        </div>
      </div>
    </template>

    <div v-if="toast.show" :class="['toast', toast.type === 'error' ? 'toast-error' : 'toast-success']">
      {{ toast.message }}
    </div>
  </div>
</template>

<script>
import { login, register, getProducts, getLikeList, addLike, updateLike, deleteLike } from './api/index.js'

export default {
  name: 'App',
  data() {
    return {
      currentUser: null,
      showRegister: false,
      loginForm: { userId: '', password: '' },
      regForm: { userId: '', userName: '', email: '', account: '', password: '' },
      products: [],
      likeList: [],
      loading: false,
      submitting: false,
      showModal: false,
      isEdit: false,
      editSn: null,
      form: { productNo: '', productName: '', price: '', feeRate: '', purchaseQuantity: 1, account: '' },
      toast: { show: false, message: '', type: 'success' }
    }
  },
  computed: {
    isAdmin() { return this.currentUser?.role === 'ADMIN' }
  },
  methods: {
    async handleLogin() {
      if (!this.loginForm.userId || !this.loginForm.password) {
        return this.showToast('請填寫帳號密碼', 'error')
      }
      this.submitting = true
      try {
        const res = await login(this.loginForm)
        this.currentUser = res.data.data
        await this.loadData()
        this.showToast('登入成功')
      } catch (e) {
        this.showToast(e.response?.data?.message || '登入失敗', 'error')
      } finally { this.submitting = false }
    },

    async handleRegister() {
      this.submitting = true
      try {
        const res = await register(this.regForm)
        this.currentUser = res.data.data
        await this.loadData()
        this.showToast('註冊成功')
      } catch (e) {
        this.showToast(e.response?.data?.message || '註冊失敗', 'error')
      } finally { this.submitting = false }
    },

    logout() {
      this.currentUser = null
      this.likeList = []
      this.loginForm = { userId: '', password: '' }
    },

    async loadData() {
      this.loading = true
      try {
        const [pRes, lRes] = await Promise.all([
          getProducts(),
          getLikeList(this.currentUser.userId)
        ])
        this.products = pRes.data.data || []
        this.likeList = lRes.data.data || []
      } catch (e) {
        this.showToast('載入資料失敗', 'error')
      } finally { this.loading = false }
    },

    onProductChange() {
      if (this.isEdit) {
        const prod = this.products.find(p => p.no === this.form.productNo)
        if (prod) {
          this.form.productName = prod.productName
          this.form.price = prod.price
          this.form.feeRate = prod.feeRate
        }
      }
    },

    openAddModal() {
      this.isEdit = false
      this.editSn = null
      this.form = {
        productNo: '', productName: '', price: '', feeRate: '',
        purchaseQuantity: 1, account: this.currentUser.account
      }
      this.showModal = true
    },

    openEditModal(item) {
      this.isEdit = true
      this.editSn = item.sn
      const prod = this.products.find(p => p.productName === item.productName)
      this.form = {
        productNo: prod?.no || '',
        productName: item.productName,
        price: item.price,
        feeRate: item.feeRate,
        purchaseQuantity: item.purchaseQuantity,
        account: item.account
      }
      this.showModal = true
    },

    closeModal() { this.showModal = false },

    async submitForm() {
      if (!this.form.productNo || !this.form.purchaseQuantity || !this.form.account) {
        return this.showToast('請填寫完整資料', 'error')
      }
      this.submitting = true
      try {
        const payload = {
          userId: this.currentUser.userId,
          productNo: this.form.productNo,
          purchaseQuantity: this.form.purchaseQuantity,
          account: this.form.account
        }
        if (this.isEdit) {
          payload.productName = this.form.productName
          payload.price = this.form.price
          payload.feeRate = this.form.feeRate
          await updateLike(this.editSn, payload, this.currentUser.userId)
          this.showToast('更新成功')
        } else {
          await addLike(payload)
          this.showToast('新增成功')
        }
        this.closeModal()
        await this.loadData()
      } catch (e) {
        this.showToast(e.response?.data?.message || '操作失敗', 'error')
      } finally { this.submitting = false }
    },

    async confirmDelete(sn) {
      if (!confirm('確定要刪除此筆喜好商品嗎？')) return
      try {
        await deleteLike(sn, this.currentUser.userId)
        this.showToast('刪除成功')
        await this.loadData()
      } catch (e) {
        this.showToast(e.response?.data?.message || '刪除失敗', 'error')
      }
    },

    fmt(n) {
      return Number(n).toLocaleString('zh-TW', { minimumFractionDigits: 2 })
    },

    showToast(message, type = 'success') {
      this.toast = { show: true, message, type }
      setTimeout(() => { this.toast.show = false }, 3000)
    }
  }
}
</script>

<style scoped>
.app { min-height: 100vh; }

.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg);
}
.login-card {
  background: #fff;
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 40px;
  width: 400px;
  max-width: 90vw;
  box-shadow: var(--shadow);
}
.login-title {
  font-size: 1.25rem;
  font-weight: 700;
  text-align: center;
  margin-bottom: 4px;
  color: var(--text);
}
.login-subtitle {
  text-align: center;
  color: var(--text-muted);
  font-size: .8rem;
  margin-bottom: 24px;
}
.login-switch {
  text-align: center;
  margin-top: 16px;
  font-size: .8rem;
  color: var(--text-muted);
}
.login-switch a {
  color: var(--primary);
  cursor: pointer;
  text-decoration: none;
}
.login-switch a:hover { text-decoration: underline; }

.app-header {
  position: sticky; top: 0; z-index: 100;
  background: #fff;
  border-bottom: 1px solid var(--border);
  padding: 12px 32px;
}
.header-inner {
  max-width: 1200px; margin: 0 auto;
  display: flex; align-items: center; justify-content: space-between;
}
.logo { font-size: 1rem; font-weight: 600; color: var(--text); }
.header-right { display: flex; align-items: center; gap: 10px; }
.user-name { font-size: .875rem; color: var(--text-sec); }
.user-badge {
  padding: 2px 8px;
  font-size: .7rem;
  font-weight: 600;
  border-radius: 99px;
  letter-spacing: .03em;
}
.badge-admin { background: rgba(9,105,218,.1); color: var(--primary); }
.badge-user { background: rgba(101,109,118,.1); color: var(--text-sec); }

.main-content { max-width: 1200px; margin: 0 auto; padding: 24px 32px; }

.toolbar {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 16px;
}
</style>
