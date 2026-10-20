import { mount, createLocalVue } from '@vue/test-utils';
import VueRouter from 'vue-router';
import ElementUI from 'element-ui';
import Breadcrumb from '@/components/Breadcrumb/index.vue';

const localVue = createLocalVue();
localVue.use(VueRouter);
localVue.use(ElementUI);

const routes = [
    {
        'path': '/',
        'children': [{
            'path': 'dashboard',
            'meta': { 'title': 'Dashboard' }
        }]
    },
    {
        'path': '/menu',
        'meta': { 'title': 'Menu' },
        'children': [{
            'path': 'menu1',
            'meta': { 'title': 'menu1' },
            'children': [{
                'path': 'menu1-1',
                'meta': { 'title': 'menu1-1' }
            },
            {
                'path': 'menu1-2',
                'redirect': 'noredirect',
                'meta': { 'title': 'menu1-2' },
                'children': [{
                    'path': 'menu1-2-1',
                    'meta': { 'title': 'menu1-2-1' }
                },
                {
                    'path': 'menu1-2-2'
                }]
            }]
        }]
    }];

const router = new VueRouter({
    routes
});

// 忽略重复导航产生的 NavigationDuplicated 拒绝
const push = (path: string) => router.push(path).catch(() => undefined);

describe('Breadcrumb.vue', () => {
    const wrapper = mount(Breadcrumb, {
        localVue,
        router

    });

    it('dashboard', async () => {
        await push('/dashboard');
        await wrapper.vm.$nextTick();
        const len = wrapper.findAll('.el-breadcrumb__inner').length;
        expect(len).toBe(1);
    });

    it('normal route', async () => {
        await push('/menu/menu1');
        await wrapper.vm.$nextTick();
        const len = wrapper.findAll('.el-breadcrumb__inner').length;
        expect(len).toBe(2);
    });

    it('nested route', async () => {
        await push('/menu/menu1/menu1-2/menu1-2-1');
        await wrapper.vm.$nextTick();
        const len = wrapper.findAll('.el-breadcrumb__inner').length;
        expect(len).toBe(4);
    });

    it('no meta.title', async () => {
        await push('/menu/menu1/menu1-2/menu1-2-2');
        await wrapper.vm.$nextTick();
        const len = wrapper.findAll('.el-breadcrumb__inner').length;
        expect(len).toBe(3);
    });

    it('click link', async () => {
        await push('/menu/menu1/menu1-2/menu1-2-2');
        await wrapper.vm.$nextTick();
        const breadcrumbArray = wrapper.findAll('.el-breadcrumb__inner');
        const second = breadcrumbArray.at(1);
        const href = second.find('a').text();
        expect(href).toBe('menu1');
    });

    it('noredirect', async () => {
        await push('/menu/menu1/menu1-2/menu1-2-1');
        await wrapper.vm.$nextTick();
        const breadcrumbArray = wrapper.findAll('.el-breadcrumb__inner');
        const redirectBreadcrumb = breadcrumbArray.at(2);
        expect(redirectBreadcrumb.find('a').exists()).toBe(false);
    });

    it('last breadcrumb', async () => {
        await push('/menu/menu1/menu1-2/menu1-2-1');
        await wrapper.vm.$nextTick();
        const breadcrumbArray = wrapper.findAll('.el-breadcrumb__inner');
        const redirectBreadcrumb = breadcrumbArray.at(3);
        expect(redirectBreadcrumb.find('a').exists()).toBe(false);
    });
});
